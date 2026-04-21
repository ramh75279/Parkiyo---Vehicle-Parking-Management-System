package com.parkiyo.parkiyo.service;

import com.parkiyo.parkiyo.dto.VehicleRequest;
import com.parkiyo.parkiyo.enums.VehicleCategory;
import com.parkiyo.parkiyo.model.ParkingRecord;
import com.parkiyo.parkiyo.model.User;
import com.parkiyo.parkiyo.model.Vehicle;
import com.parkiyo.parkiyo.repository.ParkingRecordRepository;
import com.parkiyo.parkiyo.repository.UserRepository;
import com.parkiyo.parkiyo.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private static final String IMPORT_SESSION_KEY = "vehicleImport.pending";

    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final ParkingRecordRepository parkingRecordRepository;

    public List<Vehicle> getAllVehicles(String search, String category) {
        List<Vehicle> vehicles = vehicleRepository.findAll();

        if (search != null && !search.isBlank()) {
            String q = search.toLowerCase();
            vehicles = vehicles.stream()
                    .filter(v -> v.getLicensePlate().toLowerCase().contains(q)
                            || (v.getMake() != null && v.getMake().toLowerCase().contains(q))
                            || (v.getModel() != null && v.getModel().toLowerCase().contains(q)))
                    .toList();
        }
        if (category != null && !category.isBlank()) {
            VehicleCategory cat = VehicleCategory.valueOf(category.toUpperCase());
            vehicles = vehicles.stream().filter(v -> v.getCategory() == cat).toList();
        }
        return vehicles;
    }

    public Vehicle getVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found: " + id));
    }

    public List<Vehicle> getVehiclesByUser(String email) {
        return vehicleRepository.findByUserEmail(email);
    }

    public List<Vehicle> getVehiclesByCategory(String category) {
        if (category == null || category.isBlank()) return vehicleRepository.findAll();
        return vehicleRepository.findByCategory(VehicleCategory.valueOf(category.toUpperCase()));
    }

    public List<String> getAllCategories() {
        return Arrays.stream(VehicleCategory.values()).map(Enum::name).toList();
    }

    public List<ParkingRecord> getVehicleParkingHistory(Long vehicleId) {
        return parkingRecordRepository.findByVehicleId(vehicleId);
    }

    @Transactional
    public void createVehicle(VehicleRequest request) {
        if (vehicleRepository.existsByLicensePlate(request.getLicensePlate())) {
            throw new RuntimeException("License plate already registered: " + request.getLicensePlate());
        }
        User user = request.getUserId() != null
                ? userRepository.findById(request.getUserId()).orElse(null)
                : null;

        Vehicle vehicle = Vehicle.builder()
                .licensePlate(request.getLicensePlate().toUpperCase())
                .category(request.getCategory())
                .make(request.getMake())
                .model(request.getModel())
                .color(request.getColor())
                .year(request.getYear())
                .user(user)
                .active(true)
                .build();
        vehicleRepository.save(vehicle);
    }

    @Transactional
    public void updateVehicle(Long id, VehicleRequest request) {
        Vehicle vehicle = getVehicleById(id);
        vehicle.setLicensePlate(request.getLicensePlate().toUpperCase());
        vehicle.setCategory(request.getCategory());
        vehicle.setMake(request.getMake());
        vehicle.setModel(request.getModel());
        vehicle.setColor(request.getColor());
        vehicle.setYear(request.getYear());
        vehicleRepository.save(vehicle);
    }

    @Transactional
    public void deleteVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }

    @Transactional
    public void quickRegisterByPlate(String licensePlate, String category) {
        if (vehicleRepository.existsByLicensePlate(licensePlate)) {
            throw new RuntimeException("License plate already registered: " + licensePlate);
        }
        VehicleCategory cat = (category != null && !category.isBlank())
                ? VehicleCategory.valueOf(category.toUpperCase())
                : VehicleCategory.CAR;

        Vehicle vehicle = Vehicle.builder()
                .licensePlate(licensePlate.toUpperCase())
                .category(cat)
                .active(true)
                .build();
        vehicleRepository.save(vehicle);
    }

    public void uploadImportFile(MultipartFile file, HttpSession session) {
        if (file.isEmpty()) throw new RuntimeException("Uploaded file is empty.");

        String originalFilename = file.getOriginalFilename() != null ? file.getOriginalFilename() : "vehicles-import";
        String extension = getFileExtension(originalFilename);
        if (!"csv".equals(extension) && !"xlsx".equals(extension)) {
            throw new RuntimeException("Unsupported file format. Please upload a .csv or .xlsx file.");
        }

        List<StagedVehicleRow> stagedRows = importVehiclesFromFile(file, extension);
        if (stagedRows.isEmpty()) {
            throw new RuntimeException("No data rows found in file.");
        }

        PendingVehicleImport pending = new PendingVehicleImport(
                originalFilename,
                formatFileSize(file.getSize()),
                stagedRows
        );
        session.setAttribute(IMPORT_SESSION_KEY, pending);
    }

    @Transactional
    public int confirmImport(HttpSession session,
                             boolean skipDuplicates,
                             boolean importWithWarnings,
                             boolean setAllActive) {
        PendingVehicleImport pending = getPendingImport(session);
        if (pending == null || pending.rows().isEmpty()) {
            throw new RuntimeException("No staged import data found.");
        }

        int importedCount = 0;
        for (StagedVehicleRow row : pending.rows()) {
            if ("ERROR".equals(row.status())) {
                continue;
            }
            if ("WARN".equals(row.status()) && !importWithWarnings) {
                continue;
            }

            String plate = row.licensePlate();
            if (vehicleRepository.existsByLicensePlate(plate)) {
                if (skipDuplicates) {
                    continue;
                }
                continue;
            }

            User user = null;
            if (row.ownerEmail() != null && !row.ownerEmail().isBlank()) {
                user = userRepository.findByEmail(row.ownerEmail()).orElse(null);
            }

            Vehicle vehicle = Vehicle.builder()
                    .licensePlate(plate)
                    .category(row.category())
                    .make(row.make())
                    .model(row.model())
                    .color(row.color())
                    .year(row.year())
                    .user(user)
                    .active(setAllActive)
                    .build();

            vehicleRepository.save(vehicle);
            importedCount++;
        }

        session.removeAttribute(IMPORT_SESSION_KEY);
        return importedCount;
    }

    public void clearPendingImport(HttpSession session) {
        session.removeAttribute(IMPORT_SESSION_KEY);
    }

    public Map<String, Object> getImportPreview(HttpSession session) {
        PendingVehicleImport pending = getPendingImport(session);
        if (pending == null) {
            return Map.of();
        }

        List<Map<String, Object>> validationRows = pending.rows().stream()
                .map(row -> {
                    Map<String, Object> vm = new HashMap<>();
                    vm.put("rowNum", row.rowNum());
                    vm.put("plate", row.licensePlate());
                    vm.put("makeModel", buildMakeModel(row.make(), row.model()));
                    vm.put("category", row.category() != null ? row.category().name() : "-");
                    vm.put("owner", row.ownerEmail() != null ? row.ownerEmail() : "-");
                    vm.put("status", row.status());
                    vm.put("badgeLabel", "WARN".equals(row.status()) ? "Duplicate" : null);
                    vm.put("message", row.message());
                    return vm;
                })
                .toList();

        long readyCount = pending.rows().stream().filter(r -> "OK".equals(r.status())).count();
        long warnCount = pending.rows().stream().filter(r -> "WARN".equals(r.status())).count();
        long errorCount = pending.rows().stream().filter(r -> "ERROR".equals(r.status())).count();

        Map<String, Object> uploadedFile = new HashMap<>();
        uploadedFile.put("name", pending.fileName());
        uploadedFile.put("sizeLabel", pending.fileSizeLabel());

        Map<String, Object> data = new HashMap<>();
        data.put("uploadedFile", uploadedFile);
        data.put("validationRows", validationRows);
        data.put("readyCount", readyCount);
        data.put("warnCount", warnCount);
        data.put("errorCount", errorCount);
        data.put("totalRows", pending.rows().size());
        return data;
    }

    private List<StagedVehicleRow> importVehiclesFromFile(MultipartFile file, String extension) {
        try {
            return "xlsx".equals(extension) ? parseExcel(file) : parseCsv(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse import file.", e);
        }
    }

    private List<StagedVehicleRow> parseCsv(MultipartFile file) throws IOException {
        List<RawVehicleRow> rawRows = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String headerLine = reader.readLine();
            if (headerLine == null) {
                return List.of();
            }

            Map<String, Integer> headerIndex = buildHeaderIndex(parseCsvLine(headerLine));
            String line;
            int rowNum = 2;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    rowNum++;
                    continue;
                }

                List<String> cols = parseCsvLine(line);
                rawRows.add(new RawVehicleRow(
                        rowNum,
                        getColumn(cols, headerIndex, "licensePlate", "plate", "license_plate"),
                        getColumn(cols, headerIndex, "category", "vehicleCategory", "vehicle_category"),
                        getColumn(cols, headerIndex, "make"),
                        getColumn(cols, headerIndex, "model"),
                        getColumn(cols, headerIndex, "color"),
                        getColumn(cols, headerIndex, "year"),
                        getColumn(cols, headerIndex, "ownerEmail", "email", "userEmail", "owner")
                ));
                rowNum++;
            }
        }

        return validateRawRows(rawRows);
    }

    private List<StagedVehicleRow> parseExcel(MultipartFile file) throws IOException {
        List<RawVehicleRow> rawRows = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getNumberOfSheets() > 0 ? workbook.getSheetAt(0) : null;
            if (sheet == null || sheet.getPhysicalNumberOfRows() == 0) {
                return List.of();
            }

            DataFormatter formatter = new DataFormatter();
            Row headerRow = sheet.getRow(sheet.getFirstRowNum());
            Map<String, Integer> headerIndex = new HashMap<>();
            for (Cell cell : headerRow) {
                headerIndex.put(normalizeHeader(formatter.formatCellValue(cell)), cell.getColumnIndex());
            }

            int first = sheet.getFirstRowNum() + 1;
            int last = sheet.getLastRowNum();
            for (int rowNum = first; rowNum <= last; rowNum++) {
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    continue;
                }

                String plate = getColumn(row, formatter, headerIndex, "licensePlate", "plate", "license_plate");
                String category = getColumn(row, formatter, headerIndex, "category", "vehicleCategory", "vehicle_category");
                String make = getColumn(row, formatter, headerIndex, "make");
                String model = getColumn(row, formatter, headerIndex, "model");
                String color = getColumn(row, formatter, headerIndex, "color");
                String year = getColumn(row, formatter, headerIndex, "year");
                String ownerEmail = getColumn(row, formatter, headerIndex, "ownerEmail", "email", "userEmail", "owner");

                if ((plate == null || plate.isBlank())
                        && (category == null || category.isBlank())
                        && (make == null || make.isBlank())
                        && (model == null || model.isBlank())
                        && (color == null || color.isBlank())
                        && (year == null || year.isBlank())
                        && (ownerEmail == null || ownerEmail.isBlank())) {
                    continue;
                }

                rawRows.add(new RawVehicleRow(rowNum + 1, plate, category, make, model, color, year, ownerEmail));
            }
        }

        return validateRawRows(rawRows);
    }

    private List<StagedVehicleRow> validateRawRows(List<RawVehicleRow> rawRows) {
        List<StagedVehicleRow> staged = new ArrayList<>();
        Set<String> fileSeenPlates = new HashSet<>();

        for (RawVehicleRow raw : rawRows) {
            String plate = raw.licensePlate() != null ? raw.licensePlate().trim().toUpperCase(Locale.ROOT) : "";
            String status = "OK";
            String message = null;

            if (plate.isBlank()) {
                status = "ERROR";
                message = "License plate is required.";
            }

            VehicleCategory category = null;
            if (!"ERROR".equals(status)) {
                String categoryText = raw.category() != null ? raw.category().trim().toUpperCase(Locale.ROOT) : "";
                if (categoryText.isBlank()) {
                    category = VehicleCategory.CAR;
                } else {
                    try {
                        category = VehicleCategory.valueOf(categoryText);
                    } catch (IllegalArgumentException ex) {
                        status = "ERROR";
                        message = "Invalid category: " + raw.category();
                    }
                }
            }

            if (!"ERROR".equals(status) && fileSeenPlates.contains(plate)) {
                status = "ERROR";
                message = "Duplicate plate in import file.";
            }
            fileSeenPlates.add(plate);

            if ("OK".equals(status) && vehicleRepository.existsByLicensePlate(plate)) {
                status = "WARN";
                message = "License plate already exists.";
            }

            Integer year = null;
            if (raw.year() != null && !raw.year().isBlank()) {
                try {
                    year = Integer.valueOf(raw.year().trim());
                } catch (NumberFormatException ex) {
                    status = "ERROR";
                    message = "Invalid year: " + raw.year();
                }
            }

            staged.add(new StagedVehicleRow(
                    raw.rowNum(),
                    plate,
                    category,
                    normalizeNullable(raw.make()),
                    normalizeNullable(raw.model()),
                    normalizeNullable(raw.color()),
                    year,
                    normalizeNullable(raw.ownerEmail()),
                    status,
                    message
            ));
        }

        return staged;
    }

    private String normalizeNullable(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isBlank() ? null : trimmed;
    }

    private String buildMakeModel(String make, String model) {
        if (make == null && model == null) {
            return "-";
        }
        if (make == null) {
            return model;
        }
        if (model == null) {
            return make;
        }
        return make + " " + model;
    }

    private PendingVehicleImport getPendingImport(HttpSession session) {
        Object obj = session.getAttribute(IMPORT_SESSION_KEY);
        if (obj instanceof PendingVehicleImport pending) {
            return pending;
        }
        return null;
    }

    private String getFileExtension(String filename) {
        int idx = filename.lastIndexOf('.');
        if (idx < 0 || idx == filename.length() - 1) {
            return "";
        }
        return filename.substring(idx + 1).toLowerCase(Locale.ROOT);
    }

    private String formatFileSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        }
        if (bytes < 1024 * 1024) {
            return (bytes / 1024) + " KB";
        }
        return String.format(Locale.ROOT, "%.1f MB", bytes / (1024.0 * 1024.0));
    }

    private Map<String, Integer> buildHeaderIndex(List<String> headers) {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < headers.size(); i++) {
            map.put(normalizeHeader(headers.get(i)), i);
        }
        return map;
    }

    private String normalizeHeader(String header) {
        return header == null ? "" : header.replace("_", "").replace(" ", "").trim().toLowerCase(Locale.ROOT);
    }

    private String getColumn(List<String> cols, Map<String, Integer> headerIndex, String... names) {
        for (String name : names) {
            Integer idx = headerIndex.get(normalizeHeader(name));
            if (idx != null && idx >= 0 && idx < cols.size()) {
                return cols.get(idx);
            }
        }
        return null;
    }

    private String getColumn(Row row, DataFormatter formatter, Map<String, Integer> headerIndex, String... names) {
        for (String name : names) {
            Integer idx = headerIndex.get(normalizeHeader(name));
            if (idx != null) {
                Cell cell = row.getCell(idx);
                if (cell != null) {
                    return formatter.formatCellValue(cell);
                }
            }
        }
        return null;
    }

    private List<String> parseCsvLine(String line) {
        List<String> cols = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                cols.add(current.toString().trim());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        cols.add(current.toString().trim());
        return cols;
    }

    private record RawVehicleRow(
            int rowNum,
            String licensePlate,
            String category,
            String make,
            String model,
            String color,
            String year,
            String ownerEmail
    ) {}

    private record StagedVehicleRow(
            int rowNum,
            String licensePlate,
            VehicleCategory category,
            String make,
            String model,
            String color,
            Integer year,
            String ownerEmail,
            String status,
            String message
    ) {}

    private record PendingVehicleImport(
            String fileName,
            String fileSizeLabel,
            List<StagedVehicleRow> rows
    ) {}
    }
}

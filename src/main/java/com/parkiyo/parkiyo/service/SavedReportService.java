package com.parkiyo.parkiyo.service;

import com.parkiyo.parkiyo.dto.SavedReportRequest;
import com.parkiyo.parkiyo.enums.SavedReportStatus;
import com.parkiyo.parkiyo.enums.SavedReportType;
import com.parkiyo.parkiyo.exception.ResourceNotFoundException;
import com.parkiyo.parkiyo.model.SavedReport;
import com.parkiyo.parkiyo.repository.SavedReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SavedReportService {

    private final SavedReportRepository savedReportRepository;

    @Transactional(readOnly = true)
    public List<SavedReport> findFiltered(SavedReportType type, SavedReportStatus status, String q) {
        String term = (q != null && !q.isBlank()) ? q.trim() : null;
        Specification<SavedReport> spec = (root, query, cb) -> cb.conjunction();

        if (type != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("reportType"), type));
        }
        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }
        if (term != null) {
            String pattern = "%" + term.toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("title")), pattern),
                    cb.like(cb.lower(cb.coalesce(root.get("description"), "")), pattern)
            ));
        }

        return savedReportRepository.findAll(spec, Sort.by(Sort.Direction.DESC, "updatedAt"));
    }

    @Transactional(readOnly = true)
    public SavedReport getById(Long id) {
        return savedReportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Saved report not found: " + id));
    }

    @Transactional
    public SavedReport create(SavedReportRequest request) {
        SavedReport entity = SavedReport.builder()
                .title(request.getTitle().trim())
                .description(blankToNull(request.getDescription()))
                .reportType(request.getReportType())
                .status(request.getStatus())
                .build();
        return savedReportRepository.save(entity);
    }

    @Transactional
    public SavedReport update(Long id, SavedReportRequest request) {
        SavedReport entity = getById(id);
        entity.setTitle(request.getTitle().trim());
        entity.setDescription(blankToNull(request.getDescription()));
        entity.setReportType(request.getReportType());
        entity.setStatus(request.getStatus());
        return savedReportRepository.save(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!savedReportRepository.existsById(id)) {
            throw new ResourceNotFoundException("Saved report not found: " + id);
        }
        savedReportRepository.deleteById(id);
    }

    private static String blankToNull(String s) {
        if (s == null || s.isBlank()) {
            return null;
        }
        return s.trim();
    }
}

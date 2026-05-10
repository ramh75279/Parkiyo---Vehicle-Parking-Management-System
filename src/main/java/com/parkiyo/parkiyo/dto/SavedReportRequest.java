package com.parkiyo.parkiyo.dto;

import com.parkiyo.parkiyo.enums.SavedReportStatus;
import com.parkiyo.parkiyo.enums.SavedReportType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SavedReportRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be at most 255 characters")
    private String title;

    @Size(max = 4000, message = "Description is too long")
    private String description;

    @NotNull(message = "Report type is required")
    private SavedReportType reportType;

    @NotNull(message = "Status is required")
    private SavedReportStatus status;
}

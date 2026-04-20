package com.pratik.employeedocs.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DocumentResponse {
    private Long id;
    private String employeeId;
    private String employeeName;
    private String documentType;
    private String documentNumber;
    private String fileUrl;
    private String remarks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

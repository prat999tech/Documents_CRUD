package com.pratik.employeedocs.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String employeeId;

    @NotBlank
    @Column(nullable = false)
    private String employeeName;

    @NotBlank
    @Column(nullable = false)
    private String documentType;  // e.g. PAN, JOINING_LETTER, OFFER_LETTER, etc.

    @NotBlank
    @Column(nullable = false)
    private String documentNumber;

    private String fileUrl;

    private String remarks;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

package com.pratik.employeedocs.service;

import com.pratik.employeedocs.dto.DocumentRequest;
import com.pratik.employeedocs.dto.DocumentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DocumentService {
    DocumentResponse create(DocumentRequest request);
    DocumentResponse getById(Long id);
    Page<DocumentResponse> getAll(String employeeId, String documentType, Pageable pageable);
    DocumentResponse update(Long id, DocumentRequest request);
    void delete(Long id);
}

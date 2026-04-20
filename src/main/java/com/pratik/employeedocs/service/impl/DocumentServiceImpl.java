package com.pratik.employeedocs.service.impl;

import com.pratik.employeedocs.dto.DocumentRequest;
import com.pratik.employeedocs.dto.DocumentResponse;
import com.pratik.employeedocs.entity.Document;
import com.pratik.employeedocs.exception.ResourceNotFoundException;
import com.pratik.employeedocs.repository.DocumentRepository;
import com.pratik.employeedocs.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository repository;

    @Override
    public DocumentResponse create(DocumentRequest request) {
        Document doc = Document.builder()
                .employeeId(request.getEmployeeId())
                .employeeName(request.getEmployeeName())
                .documentType(request.getDocumentType())
                .documentNumber(request.getDocumentNumber())
                .fileUrl(request.getFileUrl())
                .remarks(request.getRemarks())
                .build();
        return toResponse(repository.save(doc));
    }

    @Override
    public DocumentResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Override
    public Page<DocumentResponse> getAll(String employeeId, String documentType, Pageable pageable) {
        if (employeeId != null) return repository.findByEmployeeId(employeeId, pageable).map(this::toResponse);
        if (documentType != null) return repository.findByDocumentType(documentType, pageable).map(this::toResponse);
        return repository.findAll(pageable).map(this::toResponse);
    }

    @Override
    public DocumentResponse update(Long id, DocumentRequest request) {
        Document doc = findOrThrow(id);
        doc.setEmployeeId(request.getEmployeeId());
        doc.setEmployeeName(request.getEmployeeName());
        doc.setDocumentType(request.getDocumentType());
        doc.setDocumentNumber(request.getDocumentNumber());
        doc.setFileUrl(request.getFileUrl());
        doc.setRemarks(request.getRemarks());
        return toResponse(repository.save(doc));
    }

    @Override
    public void delete(Long id) {
        findOrThrow(id);
        repository.deleteById(id);
    }

    private Document findOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + id));
    }

    private DocumentResponse toResponse(Document doc) {
        return DocumentResponse.builder()
                .id(doc.getId())
                .employeeId(doc.getEmployeeId())
                .employeeName(doc.getEmployeeName())
                .documentType(doc.getDocumentType())
                .documentNumber(doc.getDocumentNumber())
                .fileUrl(doc.getFileUrl())
                .remarks(doc.getRemarks())
                .createdAt(doc.getCreatedAt())
                .updatedAt(doc.getUpdatedAt())
                .build();
    }
}

package com.pratik.employeedocs.controller;

import com.pratik.employeedocs.dto.ApiResponse;
import com.pratik.employeedocs.dto.DocumentRequest;
import com.pratik.employeedocs.dto.DocumentResponse;
import com.pratik.employeedocs.service.DocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService service;

    @PostMapping
    public ResponseEntity<ApiResponse<DocumentResponse>> create(@Valid @RequestBody DocumentRequest request) {
        DocumentResponse response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Document created", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DocumentResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Document fetched", service.getById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<DocumentResponse>>> getAll(
            @RequestParam(required = false) String employeeId,
            @RequestParam(required = false) String documentType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<DocumentResponse> result = service.getAll(employeeId, documentType, pageable);
        return ResponseEntity.ok(ApiResponse.success("Documents fetched", result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DocumentResponse>> update(
            @PathVariable Long id, @Valid @RequestBody DocumentRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Document updated", service.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Document deleted", null));
    }
}

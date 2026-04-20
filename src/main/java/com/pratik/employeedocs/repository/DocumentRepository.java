package com.pratik.employeedocs.repository;

import com.pratik.employeedocs.entity.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    Page<Document> findByEmployeeId(String employeeId, Pageable pageable);
    Page<Document> findByDocumentType(String documentType, Pageable pageable);
}

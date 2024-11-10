package com.tcc.backend.repositories;

import com.tcc.backend.models.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    Optional<Document> findByTitle(String titleDoc);
    Page<Document> findByTitleDocContainingIgnoreCase(String titleDoc, Pageable pageable);
}


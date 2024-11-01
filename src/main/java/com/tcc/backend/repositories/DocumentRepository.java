package com.tcc.backend.repositories;

import com.tcc.backend.models.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    Optional<Document> findByTitle(final String titleDoc);
}

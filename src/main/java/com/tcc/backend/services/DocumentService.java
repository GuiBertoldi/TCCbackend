package com.tcc.backend.services;

import com.tcc.backend.dtos.documents.DocumentRequest;
import com.tcc.backend.models.Document;
import com.tcc.backend.repositories.DocumentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class DocumentService {

    @Autowired
    public DocumentService(final DocumentRepository repository) {
        this.repository = repository;
    }

    private final DocumentRepository repository;

    public Document create(DocumentRequest request) {
        final Document newDocument = repository.save(
                Document.builder()
                        .titleDoc(request.getTitleDoc())
                        .typeDoc(request.getTypeDoc())
                        .build());
        return newDocument;
    }

    public Document update(Long idDoc, DocumentRequest request) {
        Document document = repository.findById(idDoc).orElse(null);
        Document updatedDocument = repository.save(
                Document.builder()
                        .idDoc(document.getIdDoc())
                        .titleDoc(request.getTitleDoc())
                        .typeDoc(request.getTypeDoc())
                        .build());
        return updatedDocument;
    }

    public void delete(Long idDoc) {
        Document document = repository.findById(idDoc).orElseThrow();
        repository.delete(document);
    }

    public Document getById(Long idDoc) {
        return repository.findById(idDoc).orElseThrow();
    }

    public Document getByTitleDoc(String titleDoc) {
        return repository.findByTitleDoc(titleDoc).orElseThrow();
    }

    public Page<Document> list(String titleDoc, Pageable pageable ) {
        if (titleDoc != null && !titleDoc.isEmpty()) {
            return repository.findByTitleDocContainingIgnoreCase(titleDoc, pageable);
        }
        return repository.findAll(pageable);
    }
}

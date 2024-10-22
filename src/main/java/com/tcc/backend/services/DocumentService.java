package com.tcc.backend.services;

import com.tcc.backend.models.Document;
import com.tcc.backend.repositories.DocumentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

@Service
@Transactional
public class DocumentService {

    @Autowired
    public DocumentService(final DocumentRepository repository) {
        this.repository = repository;
    }

    private final DocumentRepository repository;

    public Document create(final Document document) {
        Assert.isTrue(this.getByTitle(document.getTitleDoc()).isEmpty(), "Já existe um documento cadastrado com este nome.");
        final Document newDocument = repository.save(document);
        return newDocument;
    }

    public Document update(final Document document) {
        Assert.notNull(document.getIdDoc(), "Id não informado");
        Assert.isTrue(this.getById(document.getIdDoc()).isPresent(), "Documento não encontrado");
        return repository.save(document);
    }

    public Optional<Document> getById(final Long id) {
        return repository.findById(id);
    }

    public Optional<Document> getByTitle(final String title) {
        return repository.findByTitle(title);
    }

    public Page<Document> list(Pageable pageable) {
        return repository.findAll(pageable);
    }
}

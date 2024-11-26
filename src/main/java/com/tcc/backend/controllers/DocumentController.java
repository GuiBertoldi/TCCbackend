package com.tcc.backend.controllers;

import com.tcc.backend.dtos.documents.DocumentRequest;
import com.tcc.backend.models.Document;
import com.tcc.backend.services.DocumentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    public DocumentController(final DocumentService service) {
        this.service = service;
    }

    private final DocumentService service;

    @PostMapping
    public ResponseEntity<Document> create(@RequestBody @Valid DocumentRequest request) {
        Document document = service.create(request);
        return new ResponseEntity<>(document, HttpStatus.CREATED);
    }

    @PutMapping("/{idDoc}")
    public ResponseEntity<Void> update(@PathVariable Long idDoc, @RequestBody @Valid DocumentRequest request) {
        service.update(idDoc, request);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @DeleteMapping("/{idDoc}")
    public ResponseEntity<Void> delete(@PathVariable Long idDoc) {
        service.delete(idDoc);
        return new ResponseEntity <>(HttpStatus.OK);
    }

    //fix trocar para getID
    @GetMapping("{idDoc}")
    public ResponseEntity<Void> findByTitleDoc(@RequestParam String titleDoc) {
        service.getByTitleDoc(titleDoc);
        return new ResponseEntity <>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<Document>> list(@RequestParam(required = false) String titleDoc, Pageable pageable) {
        Page<Document> documentList = service.list(titleDoc, pageable);
        return new ResponseEntity<>(documentList, HttpStatus.OK);
    }
}

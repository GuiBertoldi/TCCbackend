package com.tcc.backend.controllers;

import com.tcc.backend.models.Document;
import com.tcc.backend.services.DocumentService;
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
    public ResponseEntity<Object> create(@RequestBody final Document documents) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(documents));
    }

    @PutMapping
    public ResponseEntity<Object> update(@RequestBody final Document documents) {
        return ResponseEntity.ok(service.update(documents));
    }

    @GetMapping("/title")
    public ResponseEntity<Object> findByTitle(@RequestParam String title) {
        return ResponseEntity.ok(service.getByTitle(title));
    }

    @GetMapping
    public Page<Document> list(Pageable pageable) {
        return service.list(pageable);
    }
}

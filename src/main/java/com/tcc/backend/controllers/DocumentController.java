package com.tcc.backend.controllers;

import com.tcc.backend.dtos.documents.DocumentRequest;
import com.tcc.backend.models.Document;
import com.tcc.backend.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    public DocumentController(final DocumentService service) {
        this.service = service;
    }

    private final DocumentService service;
    //Replicar para todos as rotas que precisam retornar algo, de modo geral os gets e remover esta merda do create
    @PostMapping
    public ResponseEntity<Document> create(@RequestBody DocumentRequest request) {
        Document document = service.create(request);
        return new ResponseEntity<>(document, HttpStatus.CREATED);
    }

    @PutMapping("/{idDoc}")
    public ResponseEntity<Void> update(@PathVariable Long idDoc, @RequestBody DocumentRequest request) {
        service.update(idDoc, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    //alterar para getbyid e adicionar em todos as controllers
    //fix trocar para getID
    @GetMapping("{idDoc}")
    public ResponseEntity<Void> findByTitle(@RequestParam String titleDoc) {
        service.getByTitle(titleDoc);
        return new ResponseEntity <>(HttpStatus.OK);
    }

    @DeleteMapping("{idDoc}")
    public ResponseEntity<Void> delete(@RequestParam Long idDoc) {
        service.delete(idDoc);
        return new ResponseEntity <>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<Document>> list(@RequestParam(required = false) String titleDoc, Pageable pageable) {
        Page<Document> documentList = service.list(titleDoc, pageable);
        return new ResponseEntity<>(documentList, HttpStatus.OK);
    }
}

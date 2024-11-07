package com.tcc.backend.controllers;

import com.tcc.backend.dtos.documents.DocumentRequest;
import com.tcc.backend.models.Document;
import com.tcc.backend.models.Patient;
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
    //Para listar todos os documentos com nome parecido utilizar o list com like, dar uma olhada no gothan sbpagamento
    //alterar para getbyid e adicionar em todos as controllers
    @GetMapping("{titleDoc}")
    public ResponseEntity<Void> findByTitle(@RequestParam String titleDoc) {
        service.getByTitle(titleDoc);
        return new ResponseEntity <>(HttpStatus.OK);
    }

    @DeleteMapping("{idDoc}")
    public ResponseEntity<Void> delete(@RequestParam Long idDoc) {
        service.delete(idDoc);
        return new ResponseEntity <>(HttpStatus.OK);
    }
    //para casos mais especificos utilizar query param, dessa forma é possível listar utilizando mais atributos no filtro
    @GetMapping("/list")
    public Page<Document> list(Pageable pageable) {
        return service.list(pageable);
    }
}

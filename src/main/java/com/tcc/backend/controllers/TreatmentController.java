package com.tcc.backend.controllers;

import com.tcc.backend.models.Treatment;
import com.tcc.backend.services.TreatmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/treatment")
public class TreatmentController {

    @Autowired
    public TreatmentController(final TreatmentService service) {
        this.service = service;
    }

    private final TreatmentService service;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody final Treatment treatment) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(treatment));
    }

    @PutMapping
    public ResponseEntity<Object> update(@RequestBody final Treatment treatment) {
        return ResponseEntity.ok(service.update(treatment));
    }

    @GetMapping
    public Page<Treatment> list(Pageable pageable) {
        return service.list(pageable);
    }
}

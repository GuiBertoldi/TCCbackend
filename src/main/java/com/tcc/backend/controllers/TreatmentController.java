package com.tcc.backend.controllers;

import com.tcc.backend.dtos.treatment.TreatmentRequest;
import com.tcc.backend.models.Treatment;
import com.tcc.backend.services.TreatmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/treatments")
public class TreatmentController {

    private final TreatmentService service;

    @Autowired
    public TreatmentController(TreatmentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Treatment> create(@RequestBody @Valid TreatmentRequest request) {
        Treatment treatment = service.create(request);
        return new ResponseEntity<>(treatment, HttpStatus.CREATED);
    }

    @PutMapping("/{idTreatment}")
    public ResponseEntity<Void> update(@PathVariable Long idTreatment, @RequestBody @Valid TreatmentRequest request) {
        service.update(idTreatment, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{idTreatment}")
    public ResponseEntity<Void> delete(@PathVariable Long idTreatment) {
        service.delete(idTreatment);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{idTreatment}")
    public ResponseEntity<Treatment> getById(@PathVariable Long idTreatment) {
        Treatment treatment = service.getById(idTreatment);
        return new ResponseEntity<>(treatment, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<Treatment>> list(Pageable pageable) {
        Page<Treatment> treatments = service.list(pageable);
        return new ResponseEntity<>(treatments, HttpStatus.OK);
    }
}

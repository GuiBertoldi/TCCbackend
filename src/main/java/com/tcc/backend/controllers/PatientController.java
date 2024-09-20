package com.tcc.backend.controllers;

import com.tcc.backend.models.Patient;
import com.tcc.backend.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    public PatientController(final PatientService service) {
        this.service = service;
    }

    private final PatientService service;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody final Patient patients) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(patients));
    }

    @PutMapping
    public ResponseEntity<Object> update(@RequestBody final Patient patients) {
        return ResponseEntity.ok(service.update(patients));
    }

    @GetMapping("/cpf")
    public ResponseEntity<Object> findByCpf(@RequestParam String cpf) {
        return ResponseEntity.ok(service.getByCpf(cpf));
    }

    @GetMapping("/name")
    public ResponseEntity<Object> findByName(@RequestParam String name) {
        return ResponseEntity.ok(service.getByName(name));
    }

    @GetMapping
    public Page<Patient> list(Pageable pageable) {
        return service.list(pageable);
    }
}

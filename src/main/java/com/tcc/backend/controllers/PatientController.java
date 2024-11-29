package com.tcc.backend.controllers;

import com.tcc.backend.dtos.patient.PatientRequest;
import com.tcc.backend.models.Patient;
import com.tcc.backend.services.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService service;

    @Autowired
    public PatientController(final PatientService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Patient> create(@RequestBody @Valid PatientRequest request) {
        Patient patient = service.createPatient(request);
        return new ResponseEntity<>(patient, HttpStatus.CREATED);
    }

    @PutMapping("/{idPatient}")
    public ResponseEntity<Void> update(@PathVariable Long idPatient, @RequestBody @Valid PatientRequest request) {
        service.updatePatient(idPatient, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{idPatient}")
    public ResponseEntity<Void> delete(@PathVariable Long idPatient) {
        service.delete(idPatient);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{idPatient}")
    public ResponseEntity<Patient> getById(@PathVariable Long idPatient) {
        Patient patient = service.getById(idPatient);
        return new ResponseEntity<>(patient, HttpStatus.OK);
    }

    @GetMapping("/search/{idUser}")
    public ResponseEntity<Patient> findByUserId(@PathVariable Long idUser) {
        Patient patient = service.findByUserId(idUser);
        return new ResponseEntity<>(patient, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<Patient>> list(Pageable pageable) {
        Page<Patient> patientList = service.list(pageable);
        return new ResponseEntity<>(patientList, HttpStatus.OK);
    }

}

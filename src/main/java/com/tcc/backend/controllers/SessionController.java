package com.tcc.backend.controllers;

import com.tcc.backend.models.Session;
import com.tcc.backend.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    @Autowired
    public SessionController(final SessionService service) {
        this.service = service;
    }

    private final SessionService service;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody final Session session) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(session));
    }

    @PutMapping
    public ResponseEntity<Object> update(@RequestBody final Session session) {
        return ResponseEntity.ok(service.update(session));
    }

    @GetMapping("/namePsychologist")
    public ResponseEntity<Object> findByNamePsychologist(@RequestParam String namePsychologist) {
        return ResponseEntity.ok(service.getByNamePsychologist(namePsychologist));
    }
    @GetMapping("/namePatient")
    public ResponseEntity<Object> findByNamePatient(@RequestParam String namePatient) {
        return ResponseEntity.ok(service.getByNamePatient(namePatient));
    }

    @GetMapping("/sessionDate")
    public ResponseEntity<Object> findBySessionDate(@RequestParam LocalDate sessionDate) {
        return ResponseEntity.ok(service.getBySessionDate(sessionDate));
    }

    @GetMapping("/sessionNumber")
    public ResponseEntity<Object> findBySessionNumber(@RequestParam Long sessionNumber) {
        return ResponseEntity.ok(service.getBySessionNumber(sessionNumber));
    }
}

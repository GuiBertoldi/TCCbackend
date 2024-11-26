package com.tcc.backend.controllers;

import com.tcc.backend.dtos.psychologist.PsychologistRequest;
import com.tcc.backend.models.Psychologist;
import com.tcc.backend.services.PsychologistService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/psychologists")
public class PsychologistController {

    private final PsychologistService service;

    @Autowired
    public PsychologistController(final PsychologistService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Psychologist> create(@RequestBody @Valid PsychologistRequest request) {
        Psychologist psychologist = service.createPsychologist(request);
        return new ResponseEntity<>(psychologist, HttpStatus.CREATED);
    }

    @PutMapping("/{idPsychologist}")
    public ResponseEntity<Void> update(@PathVariable Long idPsychologist, @RequestBody @Valid PsychologistRequest request) {
        service.updatePsychologist(idPsychologist, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{idPsychologist}")
    public ResponseEntity<Void> delete(@PathVariable Long idPsychologist) {
        service.delete(idPsychologist);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{idPsychologist}")
    public ResponseEntity<Psychologist> getById(@PathVariable Long idPsychologist) {
        Psychologist psychologist = service.getById(idPsychologist);
        return new ResponseEntity<>(psychologist, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<Psychologist>> list(Pageable pageable) {
        Page<Psychologist> psychologistList = service.list(pageable);
        return new ResponseEntity<>(psychologistList, HttpStatus.OK);
    }
}

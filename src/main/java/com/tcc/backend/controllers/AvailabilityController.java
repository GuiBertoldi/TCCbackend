package com.tcc.backend.controllers;

import com.tcc.backend.dtos.availability.AvailabilityRequest;
import com.tcc.backend.dtos.availability.AvailabilityResponse;
import com.tcc.backend.services.AvailabilityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/psychologists/{idPsychologist}/availabilities")
public class AvailabilityController {

    private final AvailabilityService service;

    @Autowired
    public AvailabilityController(AvailabilityService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AvailabilityResponse> create(@PathVariable Long idPsychologist,
                                                       @RequestBody @Valid AvailabilityRequest request) throws Exception {
        request.setIdPsychologist(idPsychologist);
        return ResponseEntity.status(201).body(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AvailabilityResponse> update(@PathVariable Long idPsychologist,
                                                       @PathVariable Long id,
                                                       @RequestBody @Valid AvailabilityRequest request) throws Exception {
        request.setIdPsychologist(idPsychologist);
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long idPsychologist,
                                       @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<AvailabilityResponse>> list(@PathVariable Long idPsychologist) {
        return ResponseEntity.ok(service.listByPsychologist(idPsychologist));
    }
}

package com.tcc.backend.controllers;

import com.tcc.backend.models.FollowUp;
import com.tcc.backend.services.FollowUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/follow-up")
public class FollowUpController {

    private final FollowUpService service;

    @Autowired
    public FollowUpController(final FollowUpService service) {
        this.service = service;
    }

    @PostMapping("create")
    public ResponseEntity<Object> create(@RequestBody final FollowUp followUp) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(followUp));
    }

    @PutMapping("update")
    public ResponseEntity<Object> update(@RequestBody final FollowUp followUp) {
        return ResponseEntity.ok(service.update(followUp));
    }

    @GetMapping
    public Page<FollowUp> list(Pageable pageable) {
        return service.list(pageable);
    }
}

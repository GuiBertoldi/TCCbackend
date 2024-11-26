package com.tcc.backend.controllers;

import com.tcc.backend.dtos.followup.FollowupRequest;
import com.tcc.backend.models.Followup;
import com.tcc.backend.services.FollowupService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/followups")
public class FollowupController {

    private final FollowupService service;

    @Autowired
    public FollowupController(FollowupService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Followup> create(@RequestBody @Valid FollowupRequest request) {
        Followup followup = service.create(request);
        return new ResponseEntity<>(followup, HttpStatus.CREATED);
    }

    @PutMapping("/{idFollowup}")
    public ResponseEntity<Void> update(@PathVariable Long idFollowup, @RequestBody @Valid FollowupRequest request) {
        service.update(idFollowup, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{idFollowup}")
    public ResponseEntity<Void> delete(@PathVariable Long idFollowup) {
        service.delete(idFollowup);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{idFollowup}")
    public ResponseEntity<Followup> getById(@PathVariable Long idFollowup) {
        Followup followup = service.getById(idFollowup);
        return new ResponseEntity<>(followup, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<Followup>> list(Pageable pageable) {
        Page<Followup> followups = service.listFollowups(pageable);
        return new ResponseEntity<>(followups, HttpStatus.OK);
    }
}

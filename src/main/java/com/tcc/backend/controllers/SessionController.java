package com.tcc.backend.controllers;

import com.tcc.backend.dtos.session.SessionRequest;
import com.tcc.backend.models.Session;
import com.tcc.backend.services.SessionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/sessions")
public class SessionController {

    private final SessionService service;

    @Autowired
    public SessionController(final SessionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Session> create(@RequestBody @Valid SessionRequest request) {
        Session session = service.create(request);
        return new ResponseEntity<>(session, HttpStatus.CREATED);
    }

    @PutMapping("/{idSession}")
    public ResponseEntity<Void> update(@PathVariable Long idSession, @RequestBody @Valid SessionRequest request) {
        service.update(idSession, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{idSession}")
    public ResponseEntity<Void> delete(@PathVariable Long idSession) {
        service.delete(idSession);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{idSession}")
    public ResponseEntity<Session> getById(@PathVariable Long idSession) {
        Session session = service.getById(idSession);
        return new ResponseEntity<>(session, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Session>> getSessionsByUserId(@PathVariable Long userId) {
        List<Session> sessions = service.getSessionsByUserId(userId);
        return ResponseEntity.ok(sessions);
    }

    @GetMapping
    public ResponseEntity<Page<Session>> list(Pageable pageable) {
        Page<Session> sessionList = service.list(pageable);
        return new ResponseEntity<>(sessionList, HttpStatus.OK);
    }
}

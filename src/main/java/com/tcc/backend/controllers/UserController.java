package com.tcc.backend.controllers;

import com.tcc.backend.dtos.user.UserRequest;
import com.tcc.backend.models.User;
import com.tcc.backend.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(final UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody @Valid UserRequest request) {
        User user = service.create(request);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("/{idUser}")
    public ResponseEntity<Void> update(@PathVariable Long idUser, @RequestBody @Valid UserRequest request) {
        service.update(idUser, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{idUser}")
    public ResponseEntity<Void> delete(@PathVariable Long idUser) {
        service.delete(idUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{idUser}")
    public ResponseEntity<User> getById(@PathVariable Long idUser) {
        User user = service.getById(idUser);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/cpf")
    public ResponseEntity<User> getByCpf(@RequestParam String cpf) {
        User user = service.getByCpf(cpf);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<User>> list(@RequestParam(required = false) String name, Pageable pageable) {
        Page<User> userList = service.list(name, pageable);
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }
}

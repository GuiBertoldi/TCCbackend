package com.tcc.backend.controllers;

import com.tcc.backend.models.User;
import com.tcc.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    public UserController(final UserService service) {
        this.service = service;
    }

    private final UserService service;

    @PostMapping("create")
    public ResponseEntity<Object> create(@RequestBody final User users) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(users));
    }
    ///{userId}
    @PutMapping("update")
    public ResponseEntity<Object> update(@RequestBody final User users) {
        return ResponseEntity.ok(service.update(users));
    }

    @GetMapping("/cpf")
    public ResponseEntity<Object> findByCpf(@RequestParam String cpf) {
        return ResponseEntity.ok(service.getByCpf(cpf));
    }

    @GetMapping("list")
    public Page<User> list(Pageable pageable) {
        return service.list(pageable);
    }
}

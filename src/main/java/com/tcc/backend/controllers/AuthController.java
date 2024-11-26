package com.tcc.backend.controllers;

import com.tcc.backend.config.JwtUtil;
import com.tcc.backend.dtos.login.LoginRequest;
import com.tcc.backend.dtos.login.LoginResponse;
import com.tcc.backend.models.User;
import com.tcc.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService service;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(UserService service, JwtUtil jwtUtil) {
        this.service = service;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        User user = service.getByEmail(loginRequest.getEmail());

        if (service.validatePassword(loginRequest.getPassword(), user.getPassword())) {
            String token = jwtUtil.generateToken(user.getEmail());
            return ResponseEntity.ok(new LoginResponse(token));
        } else {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }
    }
}

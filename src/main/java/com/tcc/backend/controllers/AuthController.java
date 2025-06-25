package com.tcc.backend.controllers;

import com.tcc.backend.config.JwtUtil;
import com.tcc.backend.config.exceptions.InvalidCredentialsException;
import com.tcc.backend.dtos.login.LoginRequest;
import com.tcc.backend.dtos.login.LoginResponse;
import com.tcc.backend.models.User;
import com.tcc.backend.services.UserService;
import jakarta.validation.Valid;
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
    public ResponseEntity<LoginResponse> authenticate(@RequestBody @Valid LoginRequest request) {
        User user = service.getByEmail(request.getEmail());

        if (user == null) {
            throw new InvalidCredentialsException();
        }

        if (!service.validatePassword(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String token = jwtUtil.generateToken(user.getIdUser().toString());
        LoginResponse response = new LoginResponse(token);
        return ResponseEntity.ok(response);
    }
}

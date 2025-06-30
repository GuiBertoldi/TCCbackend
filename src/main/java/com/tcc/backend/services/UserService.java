package com.tcc.backend.services;

import com.tcc.backend.dtos.user.UserRequest;
import com.tcc.backend.enums.UserType;
import com.tcc.backend.models.User;
import com.tcc.backend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserService {

    private static final String NOT_FOUND = "Usuário não encontrado.";

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public boolean validatePassword(String rawPassword, String storedPassword) {
        return passwordEncoder.matches(rawPassword, storedPassword)
                || rawPassword.equals(storedPassword);
    }

    public User create(UserRequest request) {
        String encodedPassword = null;
        if (!UserType.PACIENTE.equals(request.getType())) {
            if (request.getPassword() == null || request.getPassword().isEmpty()) {
                throw new IllegalArgumentException("Senha não pode ser nula para este tipo de usuário.");
            }
            encodedPassword = passwordEncoder.encode(request.getPassword());
        }
        User newUser = User.builder()
                .type(request.getType())
                .name(request.getName())
                .email(request.getEmail())
                .password(encodedPassword)
                .cpf(request.getCpf())
                .phone(request.getPhone())
                .cep(request.getCep())
                .city(request.getCity())
                .neighborhood(request.getNeighborhood())
                .street(request.getStreet())
                .number(request.getNumber())
                .build();
        return repository.save(newUser);
    }

    public User update(Long idUser, UserRequest request) {
        User existing = repository.findById(idUser)
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND));

        String encodedPassword;
        if (!UserType.PACIENTE.equals(request.getType())) {
            if (request.getPassword() == null || request.getPassword().isEmpty()) {
                throw new IllegalArgumentException("Senha não pode ser nula para este tipo de usuário.");
            }
            encodedPassword = passwordEncoder.encode(request.getPassword());
        } else {
            encodedPassword = existing.getPassword();
        }

        return repository.save(User.builder()
                .idUser(existing.getIdUser())
                .type(request.getType())
                .name(request.getName())
                .email(request.getEmail())
                .password(encodedPassword)
                .cpf(request.getCpf())
                .phone(request.getPhone())
                .cep(request.getCep())
                .city(request.getCity())
                .neighborhood(request.getNeighborhood())
                .street(request.getStreet())
                .number(request.getNumber())
                .build());
    }

    public void delete(Long idUser) {
        User toDelete = repository.findById(idUser)
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND));
        repository.delete(toDelete);
    }

    public User getById(Long idUser) {
        return repository.findById(idUser)
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND));
    }

    public User getByCpf(String cpf) {
        return repository.findByCpf(cpf)
                .orElseThrow(() -> new IllegalArgumentException("Usuário com CPF não encontrado."));
    }

    public User getByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário com email não encontrado."));
    }

    public List<User> getUsersByType(UserType type) {
        return repository.findByType(type);
    }

    public Page<User> list(String name, Pageable pageable) {
        if (name != null && !name.isEmpty()) {
            return repository.findByNameContainingIgnoreCase(name, pageable);
        }
        return repository.findAll(pageable);
    }
}

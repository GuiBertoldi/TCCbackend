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

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public boolean validatePassword(String rawPassword, String storedPassword) {
        return passwordEncoder.matches(rawPassword, storedPassword) ||
                rawPassword.equals(storedPassword);
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
                .complement(request.getComplement())
                .build();

        return repository.save(newUser);
    }


    public User update(Long idUser, UserRequest request) {
        User user = repository.findById(idUser).orElseThrow(() ->
                new IllegalArgumentException("Usuário não encontrado.")
        );

        String encodedPassword = null;

        if (!UserType.PACIENTE.equals(request.getType())) {
            if (request.getPassword() == null || request.getPassword().isEmpty()) {
                throw new IllegalArgumentException("Senha não pode ser nula para este tipo de usuário.");
            }
            encodedPassword = passwordEncoder.encode(request.getPassword());
        } else {
            encodedPassword = user.getPassword();
        }

        User updatedUser = repository.save(
                User.builder()
                        .idUser(user.getIdUser())
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
                        .complement(request.getComplement())
                        .build()
        );

        return updatedUser;
    }

    public void delete(Long idUser) {
        User user = repository.findById(idUser).orElseThrow(() ->
                new IllegalArgumentException("Usuário não encontrado."));
        repository.delete(user);
    }

    public User getById(Long idUser) {
        return repository.findById(idUser).orElseThrow(() ->
                new IllegalArgumentException("Usuário não encontrado."));
    }

    public User getByCpf(String cpf) {
        return repository.findByCpf(cpf).orElseThrow(() ->
                new IllegalArgumentException("Usuário com CPF não encontrado."));
    }

    public User getByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("Usuário com email não encontrado.")
        );
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
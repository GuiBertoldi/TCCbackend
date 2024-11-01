package com.tcc.backend.services;

import com.tcc.backend.models.User;
import com.tcc.backend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    public UserService(final UserRepository repository) {
        this.repository = repository;
    }

    private final UserRepository repository;

    public User create(final User user) {
        final User newUser = repository.save(user);
        return newUser;
    }

    public User update(final User user) {
        return repository.save(user);
    }

    public Optional<User> getById(final Long id) {
        return repository.findById(id);
    }

    public Optional<User> getByCpf(String cpf) {
        return repository.findByCpf(cpf);
    }

    public Page<User> list(Pageable pageable) {
        return repository.findAll(pageable);
    }
}

package com.tcc.backend.repositories;

import com.tcc.backend.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByCpf(final String cpf);
    Page<User> findByNameContainingIgnoreCase(String name, Pageable pageable);
}

package com.tcc.backend.repositories;

import com.tcc.backend.models.Patient;
import com.tcc.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<User> findByCpf(final String cpf);
    Optional<User> findByName(final String name);
}

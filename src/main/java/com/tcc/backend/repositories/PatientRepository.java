package com.tcc.backend.repositories;

import com.tcc.backend.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByCpf(final String cpf);
    Optional<Patient> findByName(final String name);
}

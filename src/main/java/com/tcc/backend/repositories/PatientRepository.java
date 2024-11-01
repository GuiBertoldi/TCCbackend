package com.tcc.backend.repositories;

import com.tcc.backend.models.Patient;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByIdPatient(final Long idPatient);
}

package com.tcc.backend.repositories;

import com.tcc.backend.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    @Query("SELECT p FROM Patient p JOIN FETCH p.idUser u WHERE u.idUser = :userId")
    Optional<Patient> findByUserId(Long userId);
}

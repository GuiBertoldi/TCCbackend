package com.tcc.backend.repositories;

import com.tcc.backend.models.Patient;
import com.tcc.backend.models.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TreatmentRepository extends JpaRepository<Treatment, Long> {
    @Query("SELECT p FROM Patient p JOIN FETCH p.idUser u WHERE u.idUser = :userId")
    Optional<Patient> findByUserId(Long userId);

    @Query("SELECT t FROM Treatment t WHERE t.idPatient.idUser.idUser = :userId")
    List<Treatment> findTreatmentsByUserId(Long userId);
}

package com.tcc.backend.repositories;

import com.tcc.backend.models.Followup;
import com.tcc.backend.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowupRepository extends JpaRepository<Followup, Long> {
    Optional<Followup> findByIdPatient(Patient patient);
}

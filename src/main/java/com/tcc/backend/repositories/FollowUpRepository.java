package com.tcc.backend.repositories;

import com.tcc.backend.models.Document;
import com.tcc.backend.models.FollowUp;
import com.tcc.backend.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowUpRepository extends JpaRepository<FollowUp, Long> {
    Optional<FollowUp> findByIdpatient(final Patient idpatient);
}

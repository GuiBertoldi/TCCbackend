package com.tcc.backend.repositories;

import com.tcc.backend.models.Patient;
import com.tcc.backend.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {

    Long countByIdPatient(Patient patient);
}

package com.tcc.backend.repositories;

import com.tcc.backend.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findByNamePsychologist(final String namePsychologist);
    Optional<Session> findByNamePatient(final String namePatient);
    Optional<Session> findBySessionDate(final LocalDate sessionDate);
    Optional<Session> findBySessionNumber(final Long sessionNumber);
    Optional<Session> findTopByPatientOrderBySessionNumberDesc(final Long idPatient);
}

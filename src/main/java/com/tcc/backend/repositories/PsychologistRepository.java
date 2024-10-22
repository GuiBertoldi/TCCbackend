package com.tcc.backend.repositories;

import com.tcc.backend.models.Psychologist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PsychologistRepository extends JpaRepository<Psychologist, Long> {
    Optional<Psychologist> findByIdPsychologist(final String idPsychologist);
}

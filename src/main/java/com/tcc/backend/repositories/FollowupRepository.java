package com.tcc.backend.repositories;

import com.tcc.backend.models.Followup;
import com.tcc.backend.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowupRepository extends JpaRepository<Followup, Long> {
    Optional<Followup> findByIdPatient(Patient patient);

    @Query("SELECT p FROM Patient p JOIN FETCH p.idUser u WHERE u.idUser = :userId")
    Optional<Followup> findByUserId(Long userId);

    @Query("SELECT t FROM Followup t WHERE t.idPatient.idUser.idUser = :userId")
    List<Followup> findFollowupsByUserId(Long userId);
}

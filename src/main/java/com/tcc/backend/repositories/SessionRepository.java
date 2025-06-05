package com.tcc.backend.repositories;

import com.tcc.backend.models.Patient;
import com.tcc.backend.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    Long countByIdPatient(Patient patient);
    @Query("SELECT s FROM Session s WHERE s.idPatient.idUser.idUser = :userId")
    List<Session> findSessionsByUserId(Long userId);
}

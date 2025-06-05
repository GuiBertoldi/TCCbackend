package com.tcc.backend.repositories;

import com.tcc.backend.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    //List<Appointment> findByIdPsychologist_IdPsychologistAndDateBetween(Long idPsychologist, LocalDate start, LocalDate end);
    //List<Appointment> findByIdPatient_IdPatient(Long idPatient);
}
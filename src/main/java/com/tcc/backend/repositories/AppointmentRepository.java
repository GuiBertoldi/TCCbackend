package com.tcc.backend.repositories;

import com.tcc.backend.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @Query("SELECT s FROM Appointment s WHERE s.patient.idUser.idUser = :userId")
    List<Appointment> findAppointmentsByUserId(Long userId);
    List<Appointment> findByPsychologist_IdPsychologistAndDate(Long idPsychologist, LocalDate date);
}
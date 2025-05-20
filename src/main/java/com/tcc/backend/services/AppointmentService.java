package com.tcc.backend.services;

import com.tcc.backend.models.Appointment;
import com.tcc.backend.repositories.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository repository;

    @Transactional
    public Appointment create(Appointment appointment) {
        Long psyId = appointment.getPsychologist().getIdPsychologist();
        LocalDate date = appointment.getDate();
        List<Appointment> conflicts = repository.findByPsychologist_IdPsychologistAndDateBetween(psyId, date, date);
        boolean hasConflict = conflicts.stream()
                .anyMatch(a -> a.getTime().equals(appointment.getTime()));
        if (hasConflict) {
            throw new IllegalArgumentException("Conflito de horário para o psicólogo.");
        }
        return repository.save(appointment);
    }

    @Transactional
    public Appointment update(Long id, Appointment appointment) {
        Appointment existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Agendamento não encontrado"));
        existing.setDate(appointment.getDate());
        existing.setTime(appointment.getTime());
        existing.setDuration(appointment.getDuration());
        existing.setPsychologist(appointment.getPsychologist());
        existing.setPatient(appointment.getPatient());
        existing.setStatus(appointment.getStatus());
        existing.setNotes(appointment.getNotes());
        return repository.save(existing);
    }

    @Transactional(readOnly = true)
    public Appointment findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Agendamento não encontrado"));
    }

    @Transactional(readOnly = true)
    public List<Appointment> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Appointment> findByPsychologistAndPeriod(Long idPsychologist,
                                                         LocalDate start,
                                                         LocalDate end) {
        return repository.findByPsychologist_IdPsychologistAndDateBetween(idPsychologist, start, end);
    }

    @Transactional(readOnly = true)
    public List<Appointment> findByPatient(Long patientId) {
        return repository.findByPatient_IdPatient(patientId);
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
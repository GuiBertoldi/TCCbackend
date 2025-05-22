package com.tcc.backend.services;

import com.tcc.backend.dtos.appointment.AppointmentRequest;
import com.tcc.backend.models.Appointment;
import com.tcc.backend.models.Patient;
import com.tcc.backend.models.Psychologist;
import com.tcc.backend.repositories.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tcc.backend.repositories.PatientRepository;
import com.tcc.backend.repositories.PsychologistRepository;

import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository repository;
    private final PatientRepository patientRepository;
    private final PsychologistRepository psychologistRepository;

    @Transactional
    public Appointment create(AppointmentRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado"));

        Psychologist psychologist = psychologistRepository.findById(request.getPsychologistId())
                .orElseThrow(() -> new IllegalArgumentException("Psicólogo não encontrado"));

        List<Appointment> conflicts = repository.findByPsychologist_IdPsychologistAndDateBetween(
                psychologist.getIdPsychologist(), request.getDate(), request.getDate());

        boolean hasConflict = conflicts.stream()
                .anyMatch(a -> a.getTime().equals(request.getTime()));

        if (hasConflict) {
            throw new IllegalArgumentException("Conflito de horário para o psicólogo.");
        }

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .psychologist(psychologist)
                .date(request.getDate())
                .time(request.getTime())
                .duration(request.getDuration())
                .status(request.getStatus())
                .notes(request.getNotes())
                .build();

        return repository.save(appointment);
    }


    @Transactional
    public Appointment update(Long id, AppointmentRequest request) {
        Appointment existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Agendamento não encontrado"));

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado"));

        Psychologist psychologist = psychologistRepository.findById(request.getPsychologistId())
                .orElseThrow(() -> new IllegalArgumentException("Psicólogo não encontrado"));

        existing.setDate(request.getDate());
        existing.setTime(request.getTime());
        existing.setDuration(request.getDuration());
        existing.setPatient(patient);
        existing.setPsychologist(psychologist);
        existing.setStatus(request.getStatus());
        existing.setNotes(request.getNotes());

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
package com.tcc.backend.services;

import com.tcc.backend.dtos.appointment.AppointmentRequest;
import com.tcc.backend.dtos.appointment.AppointmentUpdateRequest;
import com.tcc.backend.models.Appointment;
import com.tcc.backend.models.Patient;
import com.tcc.backend.models.Psychologist;
import com.tcc.backend.repositories.AppointmentRepository;
import com.tcc.backend.repositories.AvailabilityRepository;
import com.tcc.backend.repositories.PatientRepository;
import com.tcc.backend.repositories.PsychologistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository repository;
    private final PatientRepository patientRepository;
    private final PsychologistRepository psychologistRepository;
    private final AvailabilityRepository availabilityRepository;

    @Autowired
    public AppointmentService(AppointmentRepository repository,
                              PatientRepository patientRepository,
                              PsychologistRepository psychologistRepository,
                              AvailabilityRepository availabilityRepository) {
        this.repository = repository;
        this.patientRepository = patientRepository;
        this.psychologistRepository = psychologistRepository;
        this.availabilityRepository = availabilityRepository;
    }

    public Appointment create(AppointmentRequest request) {
        Patient patient = patientRepository.findById(request.getIdPatient())
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado"));

        Psychologist psychologist = psychologistRepository.findById(request.getIdPsychologist())
                .orElseThrow(() -> new IllegalArgumentException("Psicólogo não encontrado"));

        LocalTime start = request.getTime();
        LocalTime end = start.plusMinutes(request.getDuration());
        DayOfWeek dow = request.getDate().getDayOfWeek();

        boolean available = availabilityRepository
                .findByPsychologistIdPsychologistAndDayOfWeek(psychologist.getIdPsychologist(), dow)
                .stream()
                .anyMatch(a -> !start.isBefore(a.getStartTime()) && !end.isAfter(a.getEndTime()));

        if (!available) {
            throw new IllegalArgumentException("Horário fora da disponibilidade do psicólogo");
        }

        List<Appointment> existing = repository.findByPsychologist_IdPsychologistAndDate(
                psychologist.getIdPsychologist(), request.getDate()
        );
        existing.forEach(ex -> {
            LocalTime exStart = ex.getTime();
            LocalTime exEnd = exStart.plusMinutes(ex.getDuration());
            if (start.isBefore(exEnd) && exStart.isBefore(end)) {
                throw new IllegalStateException("Conflito de horário com outro agendamento");
            }
        });

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .psychologist(psychologist)
                .date(request.getDate())
                .time(start)
                .duration(request.getDuration())
                .status(request.getStatus())
                .notes(request.getNotes())
                .build();

        return repository.save(appointment);
    }

    public Appointment update(Long idAppointment, AppointmentUpdateRequest request) {
        Appointment existing = repository.findById(idAppointment)
                .orElseThrow(() -> new IllegalArgumentException("Agendamento não encontrado com o ID: " + idAppointment));

        LocalTime start = request.getTime();
        LocalTime end = start.plusMinutes(request.getDuration());
        DayOfWeek dow = request.getDate().getDayOfWeek();

        boolean available = availabilityRepository
                .findByPsychologistIdPsychologistAndDayOfWeek(existing.getPsychologist().getIdPsychologist(), dow)
                .stream()
                .anyMatch(a -> !start.isBefore(a.getStartTime()) && !end.isAfter(a.getEndTime()));

        if (!available) {
            throw new IllegalArgumentException("Horário fora da disponibilidade do psicólogo");
        }

        List<Appointment> existingSameDay = repository.findByPsychologist_IdPsychologistAndDate(
                existing.getPsychologist().getIdPsychologist(), request.getDate()
        );
        existingSameDay.stream()
                .filter(a -> !a.getIdAppointment().equals(idAppointment))
                .forEach(ex -> {
                    LocalTime exStart = ex.getTime();
                    LocalTime exEnd = exStart.plusMinutes(ex.getDuration());
                    if (start.isBefore(exEnd) && exStart.isBefore(end)) {
                        throw new IllegalStateException("Conflito de horário com outro agendamento");
                    }
                });

        existing.setDate(request.getDate());
        existing.setTime(start);
        existing.setDuration(request.getDuration());
        existing.setStatus(request.getStatus());
        existing.setNotes(request.getNotes());

        return repository.save(existing);
    }

    public void delete(Long idAppointment) {
        Appointment appointment = repository.findById(idAppointment)
                .orElseThrow(() -> new IllegalArgumentException("Agendamento não encontrado com o ID: " + idAppointment));
        repository.delete(appointment);
    }

    public Appointment getById(Long idAppointment) {
        return repository.findById(idAppointment)
                .orElseThrow(() -> new IllegalArgumentException("Agendamento não encontrado com o ID: " + idAppointment));
    }

    public Page<Appointment> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<Appointment> getAppointmentByUserId(Long userId) {
        return repository.findAppointmentsByUserId(userId);
    }
}

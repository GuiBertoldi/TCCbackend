package com.tcc.backend;

import com.tcc.backend.dtos.appointment.AppointmentRequest;
import com.tcc.backend.dtos.appointment.AppointmentUpdateRequest;
import com.tcc.backend.enums.AppointmentStatus;
import com.tcc.backend.models.Appointment;
import com.tcc.backend.models.Patient;
import com.tcc.backend.models.Psychologist;
import com.tcc.backend.models.PsychologistAvailability;
import com.tcc.backend.repositories.AppointmentRepository;
import com.tcc.backend.repositories.AvailabilityRepository;
import com.tcc.backend.repositories.PatientRepository;
import com.tcc.backend.repositories.PsychologistRepository;
import com.tcc.backend.services.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {

    @Mock private AppointmentRepository appointmentRepository;
    @Mock private PatientRepository   patientRepository;
    @Mock private PsychologistRepository psychologistRepository;
    @Mock private AvailabilityRepository availabilityRepository;
    @InjectMocks private AppointmentService appointmentService;

    private Patient     patient;
    private Psychologist psychologist;
    private Appointment existing;
    private LocalDate   today;
    private LocalTime   at10;

    @BeforeEach
    void setUp() {
        patient = Patient.builder().idPatient(1L).build();
        psychologist = Psychologist.builder().idPsychologist(2L).build();
        today = LocalDate.now();
        at10 = LocalTime.of(10, 0);
        existing = Appointment.builder()
                .idAppointment(5L)
                .patient(patient)
                .psychologist(psychologist)
                .date(today)
                .time(at10)
                .duration(60)
                .status(AppointmentStatus.SCHEDULED)
                .notes("Existing")
                .build();
    }

    @Test
    void testCreateSuccess() {
        var req = new AppointmentRequest();
        req.setIdPatient(1L);
        req.setIdPsychologist(2L);
        req.setDate(today);
        req.setTime(at10.plusHours(2));
        req.setDuration(30);
        req.setStatus(AppointmentStatus.SCHEDULED);
        req.setNotes("Note");

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(psychologistRepository.findById(2L)).thenReturn(Optional.of(psychologist));
        when(availabilityRepository.findByPsychologistIdPsychologistAndDayOfWeek(2L, today.getDayOfWeek()))
                .thenReturn(List.of(createAvailability(today.getDayOfWeek(), at10, at10.plusHours(4))));
        when(appointmentRepository.findByPsychologist_IdPsychologistAndDate(2L, today))
                .thenReturn(Collections.emptyList());

        ArgumentCaptor<Appointment> cap = ArgumentCaptor.forClass(Appointment.class);
        when(appointmentRepository.save(cap.capture())).thenAnswer(i -> i.getArgument(0));

        var out = appointmentService.create(req);

        var passed = cap.getValue();
        assertEquals(patient, passed.getPatient());
        assertEquals(psychologist, passed.getPsychologist());
        assertEquals(today, passed.getDate());
        assertEquals(at10.plusHours(2), passed.getTime());
        assertEquals(30, passed.getDuration());
        assertEquals(AppointmentStatus.SCHEDULED, passed.getStatus());
        assertEquals("Note", passed.getNotes());
    }

    @Test
    void testCreateWithExistingNoConflict() {
        var req = new AppointmentRequest();
        req.setIdPatient(1L);
        req.setIdPsychologist(2L);
        req.setDate(today);
        req.setTime(at10.plusHours(2));
        req.setDuration(30);

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(psychologistRepository.findById(2L)).thenReturn(Optional.of(psychologist));
        when(availabilityRepository.findByPsychologistIdPsychologistAndDayOfWeek(2L, today.getDayOfWeek()))
                .thenReturn(List.of(createAvailability(today.getDayOfWeek(), at10, at10.plusHours(4))));
        when(appointmentRepository.findByPsychologist_IdPsychologistAndDate(2L, today))
                .thenReturn(List.of(existing));

        ArgumentCaptor<Appointment> cap = ArgumentCaptor.forClass(Appointment.class);
        when(appointmentRepository.save(cap.capture())).thenAnswer(i -> i.getArgument(0));

        var out = appointmentService.create(req);

        assertNotNull(out);
        assertEquals(at10.plusHours(2), cap.getValue().getTime());
    }

    @Test
    void testCreatePatientNotFound() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());
        var req = new AppointmentRequest();
        req.setIdPatient(1L);
        var ex = assertThrows(
                IllegalArgumentException.class,
                () -> appointmentService.create(req)
        );
        assertEquals("Paciente não encontrado", ex.getMessage());
    }

    @Test
    void testCreatePsychologistNotFound() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(psychologistRepository.findById(2L)).thenReturn(Optional.empty());
        var req = new AppointmentRequest();
        req.setIdPatient(1L);
        req.setIdPsychologist(2L);
        var ex = assertThrows(
                IllegalArgumentException.class,
                () -> appointmentService.create(req)
        );
        assertEquals("Psicólogo não encontrado", ex.getMessage());
    }

    @Test
    void testCreateOutOfAvailability() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(psychologistRepository.findById(2L)).thenReturn(Optional.of(psychologist));
        when(availabilityRepository.findByPsychologistIdPsychologistAndDayOfWeek(eq(2L), any()))
                .thenReturn(Collections.emptyList());
        var req = new AppointmentRequest();
        req.setIdPatient(1L);
        req.setIdPsychologist(2L);
        req.setDate(today);
        req.setTime(at10);
        req.setDuration(30);
        var ex = assertThrows(
                IllegalArgumentException.class,
                () -> appointmentService.create(req)
        );
        assertEquals("Horário fora da disponibilidade do psicólogo", ex.getMessage());
    }

    @Test
    void testCreateConflict() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(psychologistRepository.findById(2L)).thenReturn(Optional.of(psychologist));
        when(availabilityRepository.findByPsychologistIdPsychologistAndDayOfWeek(eq(2L), any()))
                .thenReturn(List.of(createAvailability(today.getDayOfWeek(), at10, at10.plusHours(2))));
        when(appointmentRepository.findByPsychologist_IdPsychologistAndDate(2L, today))
                .thenReturn(List.of(existing));
        var req = new AppointmentRequest();
        req.setIdPatient(1L);
        req.setIdPsychologist(2L);
        req.setDate(today);
        req.setTime(at10.plusMinutes(30)); // 10:30–11:30
        req.setDuration(60);
        var ex = assertThrows(
                IllegalStateException.class,
                () -> appointmentService.create(req)
        );
        assertEquals("Conflito de horário com outro agendamento", ex.getMessage());
    }

    @Test
    void testUpdateSuccessDifferentDay() throws Exception {
        var req = new AppointmentUpdateRequest();
        req.setDate(today.plusDays(1));
        req.setTime(at10.plusHours(1));
        req.setDuration(45);
        req.setStatus(AppointmentStatus.COMPLETED);
        req.setNotes("Updated");

        when(appointmentRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(availabilityRepository.findByPsychologistIdPsychologistAndDayOfWeek(eq(2L), any()))
                .thenReturn(List.of(createAvailability(today.plusDays(1).getDayOfWeek(), at10, at10.plusHours(3))));
        when(appointmentRepository.findByPsychologist_IdPsychologistAndDate(2L, today.plusDays(1)))
                .thenReturn(List.of(existing));
        when(appointmentRepository.save(existing)).thenReturn(existing);

        var out = appointmentService.update(5L, req);
        assertEquals(today.plusDays(1), out.getDate());
        assertEquals(at10.plusHours(1), out.getTime());
        assertEquals(45, out.getDuration());
        assertEquals(AppointmentStatus.COMPLETED, out.getStatus());
        assertEquals("Updated", out.getNotes());
    }

    @Test
    void testUpdateWithExistingNoConflictSameDay() throws Exception {
        var req = new AppointmentUpdateRequest();
        req.setDate(today);
        req.setTime(at10.plusHours(2));
        req.setDuration(30);
        req.setStatus(AppointmentStatus.SCHEDULED);
        req.setNotes("NoConflict");

        when(appointmentRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(availabilityRepository.findByPsychologistIdPsychologistAndDayOfWeek(eq(2L), any()))
                .thenReturn(List.of(createAvailability(today.getDayOfWeek(), at10, at10.plusHours(4))));
        when(appointmentRepository.findByPsychologist_IdPsychologistAndDate(2L, today))
                .thenReturn(List.of(existing));
        when(appointmentRepository.save(existing)).thenReturn(existing);

        var out = appointmentService.update(5L, req);
        assertEquals(today, out.getDate());
        assertEquals(at10.plusHours(2), out.getTime());
        assertEquals(30, out.getDuration());
        assertEquals("NoConflict", out.getNotes());
    }

    @Test
    void testUpdateNotFound() {
        when(appointmentRepository.findById(5L)).thenReturn(Optional.empty());
        var req = new AppointmentUpdateRequest();
        var ex = assertThrows(
                IllegalArgumentException.class,
                () -> appointmentService.update(5L, req)
        );
        assertEquals("Agendamento não encontrado com o ID: 5", ex.getMessage());
    }

    @Test
    void testUpdateOutOfAvailability() {
        when(appointmentRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(availabilityRepository.findByPsychologistIdPsychologistAndDayOfWeek(eq(2L), any()))
                .thenReturn(Collections.emptyList());
        var req = new AppointmentUpdateRequest();
        req.setDate(today);
        req.setTime(at10);
        req.setDuration(30);
        var ex = assertThrows(
                IllegalArgumentException.class,
                () -> appointmentService.update(5L, req)
        );
        assertEquals("Horário fora da disponibilidade do psicólogo", ex.getMessage());
    }

    @Test
    void testUpdateConflict() {
        var other = Appointment.builder()
                .idAppointment(9L)
                .patient(patient)
                .psychologist(psychologist)
                .date(today)
                .time(at10.plusHours(1))
                .duration(60)
                .build();

        when(appointmentRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(availabilityRepository.findByPsychologistIdPsychologistAndDayOfWeek(eq(2L), any()))
                .thenReturn(List.of(createAvailability(today.getDayOfWeek(), at10, at10.plusHours(4))));
        when(appointmentRepository.findByPsychologist_IdPsychologistAndDate(2L, today))
                .thenReturn(List.of(existing, other));

        var req = new AppointmentUpdateRequest();
        req.setDate(today);
        req.setTime(at10.plusHours(1));
        req.setDuration(30);
        req.setStatus(AppointmentStatus.SCHEDULED);
        req.setNotes("");
        var ex = assertThrows(
                IllegalStateException.class,
                () -> appointmentService.update(5L, req)
        );
        assertEquals("Conflito de horário com outro agendamento", ex.getMessage());
    }

    @Test
    void testDeleteSuccess() {
        when(appointmentRepository.findById(5L)).thenReturn(Optional.of(existing));
        appointmentService.delete(5L);
        verify(appointmentRepository).delete(existing);
    }

    @Test
    void testDeleteNotFound() {
        when(appointmentRepository.findById(5L)).thenReturn(Optional.empty());
        var ex = assertThrows(
                IllegalArgumentException.class,
                () -> appointmentService.delete(5L)
        );
        assertEquals("Agendamento não encontrado com o ID: 5", ex.getMessage());
    }

    @Test
    void testGetByIdSuccess() {
        when(appointmentRepository.findById(5L)).thenReturn(Optional.of(existing));
        var a = appointmentService.getById(5L);
        assertEquals(existing, a);
    }

    @Test
    void testGetByIdNotFound() {
        when(appointmentRepository.findById(5L)).thenReturn(Optional.empty());
        var ex = assertThrows(
                IllegalArgumentException.class,
                () -> appointmentService.getById(5L)
        );
        assertEquals("Agendamento não encontrado com o ID: 5", ex.getMessage());
    }

    @Test
    void testGetAppointmentByUserId() {
        when(appointmentRepository.findAppointmentsByUserId(1L))
                .thenReturn(List.of(existing));
        var list = appointmentService.getAppointmentByUserId(1L);
        assertEquals(1, list.size());
        assertEquals(existing, list.get(0));
    }

    @Test
    void testList() {
        var page = PageRequest.of(0, 10);
        var p = new PageImpl<>(List.of(existing), page, 1);
        when(appointmentRepository.findAll(page)).thenReturn(p);
        var out = appointmentService.list(page);
        assertEquals(1, out.getTotalElements());
        assertEquals(existing, out.getContent().get(0));
    }

    private PsychologistAvailability createAvailability(DayOfWeek dow, LocalTime s, LocalTime e) {
        return PsychologistAvailability.builder()
                .id(99L)
                .psychologist(Psychologist.builder().idPsychologist(2L).build())
                .dayOfWeek(dow)
                .startTime(s)
                .endTime(e)
                .build();
    }
}

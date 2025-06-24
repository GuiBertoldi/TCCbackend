package com.tcc.backend;

import com.tcc.backend.dtos.session.SessionRequest;
import com.tcc.backend.models.Patient;
import com.tcc.backend.models.Psychologist;
import com.tcc.backend.models.Session;
import com.tcc.backend.models.User;
import com.tcc.backend.repositories.PatientRepository;
import com.tcc.backend.repositories.PsychologistRepository;
import com.tcc.backend.repositories.SessionRepository;
import com.tcc.backend.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PsychologistRepository psychologistRepository;

    @InjectMocks
    private SessionService sessionService;

    private Patient patient;
    private Psychologist psychologist;
    private Session session;

    @BeforeEach
    void setUp() {
        User patientUser = User.builder()
                .idUser(1L)
                .name("Alice Paciente")
                .build();
        patient = Patient.builder()
                .idPatient(10L)
                .idUser(patientUser)
                .build();

        User psychUser = User.builder()
                .idUser(2L)
                .name("Dr. Bob")
                .build();
        psychologist = Psychologist.builder()
                .idPsychologist(20L)
                .idUser(psychUser)
                .build();

        session = Session.builder()
                .idSession(100L)
                .idPatient(patient)
                .idPsychologist(psychologist)
                .sessionNumber(1L)
                .sessionDate(LocalDate.of(2025, 5, 1))
                .reason("Consulta inicial")
                .description("Descrição da sessão")
                .build();
    }

    @Test
    void testCreateSuccess() {
        SessionRequest req = SessionRequest.builder()
                .patientId(10L)
                .idUser(2L)
                .sessionDate(LocalDate.of(2025, 5, 1))
                .reason("Consulta inicial")
                .description("OK")
                .build();

        when(patientRepository.findByUserId(10L)).thenReturn(Optional.of(patient));
        when(psychologistRepository.findByIdUserId(2L)).thenReturn(Optional.of(psychologist));
        when(sessionRepository.countByIdPatient(patient)).thenReturn(2L);

        ArgumentCaptor<Session> capt = ArgumentCaptor.forClass(Session.class);
        when(sessionRepository.save(capt.capture())).thenReturn(session);

        Session created = sessionService.create(req);

        assertEquals(3L, capt.getValue().getSessionNumber());
        assertEquals("Consulta inicial", created.getReason());
        verify(sessionRepository).save(any());
    }

    @Test
    void testCreateFailPatientNotFound() {
        SessionRequest req = new SessionRequest();
        req.setPatientId(99L);
        when(patientRepository.findByUserId(99L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> sessionService.create(req));
        assertEquals("Paciente não encontrado com o ID: 99", ex.getMessage());
    }

    @Test
    void testCreateFailPsychologistNotFound() {
        SessionRequest req = new SessionRequest();
        req.setPatientId(10L);
        req.setIdUser(77L);

        when(patientRepository.findByUserId(10L)).thenReturn(Optional.of(patient));
        when(psychologistRepository.findByIdUserId(77L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> sessionService.create(req));
        assertEquals("Psicólogo não encontrado com o ID: 77", ex.getMessage());
    }

    @Test
    void testUpdateSuccess() {
        SessionRequest req = SessionRequest.builder()
                .patientId(10L)
                .idUser(2L)
                .sessionDate(LocalDate.of(2025, 6, 1))
                .reason("Retorno")
                .description("Tudo bem")
                .build();

        Session existing = Session.builder()
                .idSession(100L)
                .idPatient(patient)
                .idPsychologist(psychologist)
                .sessionNumber(1L)
                .sessionDate(LocalDate.of(2025, 5, 1))
                .reason("Consulta inicial")
                .description("Desc")
                .build();

        when(sessionRepository.findById(100L)).thenReturn(Optional.of(existing));
        when(patientRepository.findById(10L)).thenReturn(Optional.of(patient));
        when(psychologistRepository.findById(2L)).thenReturn(Optional.of(psychologist));
        when(sessionRepository.save(existing)).thenAnswer(inv -> inv.getArgument(0));

        Session updated = sessionService.update(100L, req);

        assertEquals("Retorno", updated.getReason());
        assertEquals(LocalDate.of(2025, 6, 1), updated.getSessionDate());
    }

    @Test
    void testUpdateFailSessionNotFound() {
        when(sessionRepository.findById(123L)).thenReturn(Optional.empty());
        SessionRequest req = new SessionRequest();
        req.setPatientId(10L);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> sessionService.update(123L, req));
        assertEquals("Sessão não encontrada com o ID: 123", ex.getMessage());
    }

    @Test
    void testUpdateFailPatientNotFound() {
        when(sessionRepository.findById(100L)).thenReturn(Optional.of(session));
        when(patientRepository.findById(55L)).thenReturn(Optional.empty());

        SessionRequest req = new SessionRequest();
        req.setPatientId(55L);
        req.setIdUser(2L);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> sessionService.update(100L, req));
        assertEquals("Paciente não encontrado com o ID: 55", ex.getMessage());
    }

    @Test
    void testUpdateFailPsychologistNotFound() {
        when(sessionRepository.findById(100L)).thenReturn(Optional.of(session));
        when(patientRepository.findById(10L)).thenReturn(Optional.of(patient));
        when(psychologistRepository.findById(88L)).thenReturn(Optional.empty());

        SessionRequest req = new SessionRequest();
        req.setPatientId(10L);
        req.setIdUser(88L);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> sessionService.update(100L, req));
        assertEquals("Psicólogo não encontrado com o ID: 88", ex.getMessage());
    }

    @Test
    void testDeleteSuccess() {
        when(sessionRepository.findById(100L)).thenReturn(Optional.of(session));
        sessionService.delete(100L);
        verify(sessionRepository).delete(session);
    }

    @Test
    void testDeleteFail() {
        when(sessionRepository.findById(200L)).thenReturn(Optional.empty());
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> sessionService.delete(200L));
        assertEquals("Sessão não encontrada com o ID: 200", ex.getMessage());
    }

    @Test
    void testGetByIdSuccess() {
        when(sessionRepository.findById(100L)).thenReturn(Optional.of(session));
        Session found = sessionService.getById(100L);
        assertEquals(100L, found.getIdSession());
    }

    @Test
    void testGetByIdFail() {
        when(sessionRepository.findById(300L)).thenReturn(Optional.empty());
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> sessionService.getById(300L));
        assertEquals("Sessão não encontrada com o ID: 300", ex.getMessage());
    }

    @Test
    void testGetSessionsByUserId() {
        List<Session> list = Collections.singletonList(session);
        when(sessionRepository.findSessionsByUserId(1L)).thenReturn(list);

        List<Session> result = sessionService.getSessionsByUserId(1L);
        assertEquals(1, result.size());
        assertSame(session, result.get(0));
    }

    @Test
    void testListPageable() {
        Page<Session> page = new PageImpl<>(List.of(session));
        Pageable pg = PageRequest.of(0, 10);

        when(sessionRepository.findAll(pg)).thenReturn(page);

        Page<Session> result = sessionService.list(pg);
        assertEquals(1, result.getTotalElements());
        assertEquals(session, result.getContent().get(0));
    }
}

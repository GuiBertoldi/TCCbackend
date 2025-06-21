package com.tcc.backend;

import com.tcc.backend.dtos.followup.FollowupRequest;
import com.tcc.backend.models.Followup;
import com.tcc.backend.models.Patient;
import com.tcc.backend.models.User;
import com.tcc.backend.repositories.FollowupRepository;
import com.tcc.backend.repositories.PatientRepository;
import com.tcc.backend.services.FollowupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FollowupServiceTest {

    @Mock
    private FollowupRepository followupRepository;

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private FollowupService followupService;

    private Patient patient;
    private Followup followup;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .idUser(1L)
                .name("Paciente Teste")
                .build();
        patient = Patient.builder()
                .idPatient(10L)
                .idUser(user)
                .build();

        followup = Followup.builder()
                .idFollowUp(100L)
                .idPatient(patient)
                .professionalName("Dr. Test")
                .professionalSpecialty("Especialidade")
                .build();
    }

    @Test
    void testCreateSuccess() {
        FollowupRequest req = FollowupRequest.builder()
                .patientId(10L)
                .professionalName("Dr. Test")
                .professionalSpecialty("Especialidade")
                .build();

        when(patientRepository.findByUserId(10L)).thenReturn(Optional.of(patient));
        ArgumentCaptor<Followup> capt = ArgumentCaptor.forClass(Followup.class);
        when(followupRepository.save(capt.capture())).thenReturn(followup);

        Followup created = followupService.create(req);

        Followup passed = capt.getValue();
        assertEquals(patient, passed.getIdPatient());
        assertEquals("Dr. Test", passed.getProfessionalName());
        assertEquals("Especialidade", passed.getProfessionalSpecialty());
        assertSame(followup, created);
    }

    @Test
    void testCreateFailPatientNotFound() {
        when(patientRepository.findByUserId(99L)).thenReturn(Optional.empty());
        FollowupRequest req = new FollowupRequest();
        req.setPatientId(99L);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> followupService.create(req));
        assertEquals("Paciente não encontrado com o ID: 99", ex.getMessage());
    }

    @Test
    void testUpdateSuccess() {
        FollowupRequest req = FollowupRequest.builder()
                .patientId(10L)
                .professionalName("Dr. Novo")
                .professionalSpecialty("Nova Especialidade")
                .build();

        when(followupRepository.findById(100L)).thenReturn(Optional.of(followup));
        when(patientRepository.findByUserId(10L)).thenReturn(Optional.of(patient));
        when(followupRepository.save(followup)).thenAnswer(inv -> inv.getArgument(0));

        Followup updated = followupService.update(100L, req);

        assertEquals("Dr. Novo", updated.getProfessionalName());
        assertEquals("Nova Especialidade", updated.getProfessionalSpecialty());
        assertEquals(patient, updated.getIdPatient());
    }

    @Test
    void testUpdateFailNotFound() {
        when(followupRepository.findById(123L)).thenReturn(Optional.empty());
        FollowupRequest req = new FollowupRequest();
        req.setPatientId(10L);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> followupService.update(123L, req));
        assertEquals("Acompanhamento não encontrado com o ID: 123", ex.getMessage());
    }

    @Test
    void testUpdateFailPatientNotFound() {
        when(followupRepository.findById(100L)).thenReturn(Optional.of(followup));
        when(patientRepository.findByUserId(55L)).thenReturn(Optional.empty());

        FollowupRequest req = new FollowupRequest();
        req.setPatientId(55L);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> followupService.update(100L, req));
        assertEquals("Paciente não encontrado com o ID: 55", ex.getMessage());
    }

    @Test
    void testDeleteSuccess() {
        when(followupRepository.findById(100L)).thenReturn(Optional.of(followup));
        followupService.delete(100L);
        verify(followupRepository).delete(followup);
    }

    @Test
    void testDeleteFail() {
        when(followupRepository.findById(200L)).thenReturn(Optional.empty());
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> followupService.delete(200L));
        assertEquals("Acompanhamento não encontrado com o ID: 200", ex.getMessage());
    }

    @Test
    void testGetByIdSuccess() {
        when(followupRepository.findById(100L)).thenReturn(Optional.of(followup));
        Followup f = followupService.getById(100L);
        assertSame(followup, f);
    }

    @Test
    void testGetByIdFail() {
        when(followupRepository.findById(300L)).thenReturn(Optional.empty());
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> followupService.getById(300L));
        assertEquals("Acompanhamento não encontrado com o ID: 300", ex.getMessage());
    }

    @Test
    void testGetByPatientSuccess() {
        when(patientRepository.findById(10L)).thenReturn(Optional.of(patient));
        when(followupRepository.findByIdPatient(patient)).thenReturn(Optional.of(followup));

        Followup f = followupService.getByPatient(10L);
        assertSame(followup, f);
    }

    @Test
    void testGetByPatientFailPatientNotFound() {
        when(patientRepository.findById(99L)).thenReturn(Optional.empty());
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> followupService.getByPatient(99L));
        assertEquals("Paciente não encontrado com o ID: 99", ex.getMessage());
    }

    @Test
    void testGetByPatientFailFollowupNotFound() {
        when(patientRepository.findById(10L)).thenReturn(Optional.of(patient));
        when(followupRepository.findByIdPatient(patient)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> followupService.getByPatient(10L));
        assertEquals("Acompanhamento não encontrado para o paciente informado.", ex.getMessage());
    }

    @Test
    void testGetFollowupsByUserId() {
        List<Followup> list = Collections.singletonList(followup);
        when(followupRepository.findFollowupsByUserId(1L)).thenReturn(list);

        List<Followup> result = followupService.getFollowupsByUserId(1L);
        assertEquals(1, result.size());
        assertSame(followup, result.get(0));
    }

    @Test
    void testListFollowups() {
        Page<Followup> page = new PageImpl<>(List.of(followup));
        Pageable pg = PageRequest.of(0, 5);

        when(followupRepository.findAll(pg)).thenReturn(page);

        Page<Followup> result = followupService.listFollowups(pg);
        assertEquals(1, result.getTotalElements());
        assertEquals(followup, result.getContent().get(0));
    }
}


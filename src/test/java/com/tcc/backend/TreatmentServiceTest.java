package com.tcc.backend;

import com.tcc.backend.dtos.treatment.TreatmentRequest;
import com.tcc.backend.models.Patient;
import com.tcc.backend.models.Treatment;
import com.tcc.backend.models.User;
import com.tcc.backend.repositories.PatientRepository;
import com.tcc.backend.repositories.TreatmentRepository;
import com.tcc.backend.services.TreatmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TreatmentServiceTest {

    @Mock
    private TreatmentRepository treatmentRepository;

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private TreatmentService treatmentService;

    private Patient patient;
    private Treatment treatment;

    @BeforeEach
    void setUp() {
        User patientUser = User.builder()
                .idUser(1L)
                .name("John Doe")
                .build();

        patient = Patient.builder()
                .idPatient(1L)
                .idUser(patientUser)
                .build();

        treatment = Treatment.builder()
                .idTreatment(1L)
                .idPatient(patient)
                .medicine("Ibuprofen")
                .startTreatment(LocalDate.of(2025, 1, 1))
                .endTreatment(LocalDate.of(2025, 2, 1))
                .build();
    }

    @Test
    void testCreateSuccess() {
        TreatmentRequest req = new TreatmentRequest();
        req.setPatientId(1L);
        req.setMedicine("Ibuprofen");
        req.setStartTreatment(LocalDate.of(2025, 1, 1));
        req.setEndTreatment(LocalDate.of(2025, 2, 1));

        when(patientRepository.findByUserId(1L)).thenReturn(Optional.of(patient));
        when(treatmentRepository.save(any(Treatment.class))).thenReturn(treatment);

        Treatment result = treatmentService.create(req);

        assertNotNull(result);
        assertEquals("Ibuprofen", result.getMedicine());
        assertEquals("John Doe", result.getIdPatient().getIdUser().getName());
        verify(treatmentRepository).save(any(Treatment.class));
    }

    @Test
    void testCreateFailPatientNotFound() {
        TreatmentRequest req = new TreatmentRequest();
        req.setPatientId(42L);
        when(patientRepository.findByUserId(42L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> treatmentService.create(req));
        assertEquals("Paciente não encontrado com o ID: 42", ex.getMessage());
    }

    @Test
    void testUpdateSuccess() {
        TreatmentRequest req = new TreatmentRequest();
        req.setPatientId(1L);
        req.setMedicine("Paracetamol");
        req.setStartTreatment(LocalDate.of(2025, 3, 1));
        req.setEndTreatment(LocalDate.of(2025, 4, 1));

        Treatment existing = Treatment.builder()
                .idTreatment(1L)
                .idPatient(patient)
                .medicine("Ibuprofen")
                .startTreatment(LocalDate.of(2025, 1, 1))
                .endTreatment(LocalDate.of(2025, 2, 1))
                .build();

        when(treatmentRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(patientRepository.findByUserId(1L)).thenReturn(Optional.of(patient));
        when(treatmentRepository.save(existing)).thenAnswer(inv -> inv.getArgument(0));

        Treatment updated = treatmentService.update(1L, req);

        assertEquals("Paracetamol", updated.getMedicine());
        assertEquals(LocalDate.of(2025, 3, 1), updated.getStartTreatment());
        assertEquals(LocalDate.of(2025, 4, 1), updated.getEndTreatment());
    }

    @Test
    void testUpdateFailTreatmentNotFound() {
        when(treatmentRepository.findById(99L)).thenReturn(Optional.empty());
        TreatmentRequest req = new TreatmentRequest();
        req.setPatientId(1L);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> treatmentService.update(99L, req));
        assertEquals("Tratamento não encontrado com o ID: 99", ex.getMessage());
    }

    @Test
    void testUpdateFailPatientNotFound() {
        Treatment existing = Treatment.builder()
                .idTreatment(2L)
                .idPatient(patient)
                .build();
        when(treatmentRepository.findById(2L)).thenReturn(Optional.of(existing));
        when(patientRepository.findByUserId(5L)).thenReturn(Optional.empty());

        TreatmentRequest req = new TreatmentRequest();
        req.setPatientId(5L);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> treatmentService.update(2L, req));
        assertEquals("Paciente não encontrado com o ID: 5", ex.getMessage());
    }

    @Test
    void testDeleteSuccess() {
        when(treatmentRepository.findById(1L)).thenReturn(Optional.of(treatment));
        treatmentService.delete(1L);
        verify(treatmentRepository).delete(treatment);
    }

    @Test
    void testDeleteFail() {
        when(treatmentRepository.findById(7L)).thenReturn(Optional.empty());
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> treatmentService.delete(7L));
        assertEquals("Tratamento não encontrado com o ID: 7", ex.getMessage());
    }

    @Test
    void testGetByIdSuccess() {
        when(treatmentRepository.findById(1L)).thenReturn(Optional.of(treatment));
        Treatment found = treatmentService.getById(1L);
        assertEquals(1L, found.getIdTreatment());
    }

    @Test
    void testGetByIdFail() {
        when(treatmentRepository.findById(8L)).thenReturn(Optional.empty());
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> treatmentService.getById(8L));
        assertEquals("Tratamento não encontrado com o ID: 8", ex.getMessage());
    }

    @Test
    void testGetTreatmentsByUserId() {
        List<Treatment> list = Collections.singletonList(treatment);
        when(treatmentRepository.findTreatmentsByUserId(1L)).thenReturn(list);

        List<Treatment> result = treatmentService.getTreatmentsByUserId(1L);
        assertEquals(1, result.size());
        assertSame(treatment, result.get(0));
    }

    @Test
    void testListPageable() {
        List<Treatment> list = Arrays.asList(treatment);
        Page<Treatment> page = new PageImpl<>(list);
        Pageable pg = PageRequest.of(0, 10);

        when(treatmentRepository.findAll(pg)).thenReturn(page);

        Page<Treatment> result = treatmentService.list(pg);
        assertEquals(1, result.getTotalElements());
        assertEquals(treatment, result.getContent().get(0));
    }
}

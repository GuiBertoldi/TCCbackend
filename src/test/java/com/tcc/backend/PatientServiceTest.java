package com.tcc.backend;

import com.tcc.backend.dtos.patient.PatientRequest;
import com.tcc.backend.dtos.user.UserRequest;
import com.tcc.backend.enums.UserType;
import com.tcc.backend.models.Patient;
import com.tcc.backend.models.User;
import com.tcc.backend.repositories.PatientRepository;
import com.tcc.backend.services.PatientService;
import com.tcc.backend.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private PatientService patientService;

    private Patient existingPatient;
    private User existingUser;

    @BeforeEach
    void setUp() {
        existingUser = new User();
        existingUser.setIdUser(10L);

        existingPatient = new Patient();
        existingPatient.setIdPatient(20L);
        existingPatient.setIdUser(existingUser);
    }

    @Test
    void testCreatePatientSuccess() {
        PatientRequest req = new PatientRequest();
        req.setName("Novo Paciente");
        req.setEmail("paciente@ex.com");
        req.setCpf("11122233344");
        req.setPhone("999888777");
        req.setCep("12345-678");
        req.setCity("CidadeX");
        req.setNeighborhood("BairroY");
        req.setStreet("RuaZ");
        req.setNumber(100);
        req.setComplement("Apto1");
        req.setEmergencyContact("ContatoX");
        req.setFatherName("PaiX");
        req.setFatherEducation("Ensino Médio");
        req.setFatherAge(50);
        req.setFatherWorkplace("EmpresaX");
        req.setFatherProfession("ProfissãoX");
        req.setMotherName("MãeY");
        req.setMotherEducation("Superior");
        req.setMotherAge(48);
        req.setMotherWorkplace("EmpresaY");
        req.setMotherProfession("ProfissãoY");

        User createdUser = new User();
        createdUser.setIdUser(10L);
        when(userService.create(any(UserRequest.class))).thenReturn(createdUser);
        when(patientRepository.save(any(Patient.class))).thenAnswer(inv -> inv.getArgument(0));

        Patient result = patientService.createPatient(req);

        assertNotNull(result);
        assertEquals(10L, result.getIdUser().getIdUser());
        assertEquals("ContatoX", result.getEmergencyContact());
        assertEquals("PaiX", result.getFatherName());
        assertEquals("MãeY", result.getMotherName());

        ArgumentCaptor<UserRequest> captor = ArgumentCaptor.forClass(UserRequest.class);
        verify(userService).create(captor.capture());
        assertEquals(UserType.PACIENTE, captor.getValue().getType());
        assertEquals("paciente@ex.com", captor.getValue().getEmail());
    }

    @Test
    void testUpdatePatientSuccess() {
        PatientRequest req = new PatientRequest();
        req.setName("Nome Paciente");
        req.setEmail("paciente@ex.com");
        req.setCpf("12345678900");
        req.setPhone("999999999");
        req.setCep("12345-678");
        req.setCity("Cidade Exemplo");
        req.setNeighborhood("Bairro Exemplo");
        req.setStreet("Rua Exemplo");
        req.setNumber(123);
        req.setComplement("Apto 456");
        req.setEmergencyContact("Novo Contato");
        req.setFatherName("Novo Pai");
        req.setFatherEducation("Educação Pai");
        req.setFatherAge(50);
        req.setFatherWorkplace("Trabalho Pai");
        req.setFatherProfession("Profissão Pai");
        req.setMotherName("Nova Mãe");
        req.setMotherEducation("Educação Mãe");
        req.setMotherAge(48);
        req.setMotherWorkplace("Trabalho Mãe");
        req.setMotherProfession("Profissão Mãe");

        when(patientRepository.findById(20L)).thenReturn(Optional.of(existingPatient));
        when(userService.update(eq(10L), any(UserRequest.class))).thenReturn(existingUser);
        when(patientRepository.save(existingPatient)).thenAnswer(inv -> inv.getArgument(0));

        Patient result = patientService.updatePatient(20L, req);

        assertNotNull(result);
        assertEquals("Novo Contato", result.getEmergencyContact());
        assertEquals("Novo Pai", result.getFatherName());
        assertEquals("Nova Mãe", result.getMotherName());

        ArgumentCaptor<UserRequest> captor = ArgumentCaptor.forClass(UserRequest.class);
        verify(userService).update(eq(10L), captor.capture());
        assertEquals("paciente@ex.com", captor.getValue().getEmail());

        verify(patientRepository).save(existingPatient);
    }

    @Test
    void testUpdatePatientNotFound() {
        when(patientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> patientService.updatePatient(99L, new PatientRequest()),
                "Paciente não encontrado com o ID: 99"
        );
    }

    @Test
    void testDeletePatientSuccess() {
        when(patientRepository.getById(20L)).thenReturn(existingPatient);
        doNothing().when(userService).delete(10L);
        doNothing().when(patientRepository).delete(existingPatient);

        patientService.delete(20L);

        verify(userService).delete(10L);
        verify(patientRepository).delete(existingPatient);
    }

    @Test
    void testGetByIdNotFound() {
        when(patientRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> patientService.getById(99L)
        );
        assertTrue(ex.getMessage().contains("Paciente não encontrado com o ID: 99"));
    }

    @Test
    void testGetByIdSuccess() {
        when(patientRepository.findById(20L)).thenReturn(Optional.of(existingPatient));
        Patient p = patientService.getById(20L);
        assertEquals(20L, p.getIdPatient());
    }

    @Test
    void testFindByUserIdNotFound() {
        when(patientRepository.findByUserId(10L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> patientService.findByUserId(10L)
        );
        assertTrue(ex.getMessage().contains("Paciente não encontrado com o ID de usuário: 10"));
    }

    @Test
    void testFindByUserIdSuccess() {
        when(patientRepository.findByUserId(10L)).thenReturn(Optional.of(existingPatient));
        Patient p = patientService.findByUserId(10L);
        assertEquals(20L, p.getIdPatient());
    }

    @Test
    void testListPatientsNonEmpty() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("idPatient"));
        Page<Patient> page = new PageImpl<>(Collections.singletonList(existingPatient), pageable, 1);
        when(patientRepository.findAll(pageable)).thenReturn(page);

        Page<Patient> result = patientService.list(pageable);
        assertEquals(1, result.getTotalElements());
        assertEquals(existingPatient, result.getContent().get(0));
    }

    @Test
    void testListPatientsEmpty() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Patient> page = new PageImpl<>(Collections.emptyList(), pageable, 0);
        when(patientRepository.findAll(pageable)).thenReturn(page);

        Page<Patient> result = patientService.list(pageable);
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
    }
}

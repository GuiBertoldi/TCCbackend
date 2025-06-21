package com.tcc.backend;

import com.tcc.backend.dtos.psychologist.PsychologistRequest;
import com.tcc.backend.models.Psychologist;
import com.tcc.backend.models.User;
import com.tcc.backend.repositories.PsychologistRepository;
import com.tcc.backend.services.PsychologistService;
import com.tcc.backend.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PsychologistServiceTest {

    @InjectMocks
    private PsychologistService psychologistService;

    @Mock
    private PsychologistRepository psychologistRepository;

    @Mock
    private UserService userService;

    private Psychologist psychologist;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        User user = User.builder()
                .idUser(1L)
                .name("Psychologist User")
                .email("psychologist@example.com")
                .build();

        psychologist = Psychologist.builder()
                .idPsychologist(1L)
                .crp("12345")
                .idUser(user)
                .build();
    }

    @Test
    void testCreatePsychologistSuccess() {
        PsychologistRequest req = PsychologistRequest.builder()
                .name("New Psychologist")
                .email("newpsychologist@example.com")
                .password("password123")
                .cpf("12345678901")
                .phone("987654321")
                .crp("67890")
                .build();

        User createdUser = User.builder()
                .idUser(1L)
                .name(req.getName())
                .email(req.getEmail())
                .build();

        when(userService.create(any())).thenReturn(createdUser);
        when(psychologistRepository.save(any(Psychologist.class))).thenReturn(psychologist);

        Psychologist result = psychologistService.createPsychologist(req);

        assertNotNull(result);
        assertEquals("12345", result.getCrp());
        verify(psychologistRepository, times(1)).save(any(Psychologist.class));
    }

    @Test
    void testUpdatePsychologistSuccess() {
        PsychologistRequest req = PsychologistRequest.builder()
                .name("Updated Psychologist")
                .email("updated@example.com")
                .password("newpass")
                .cpf("12345678901")
                .phone("987654321")
                .crp("54321")
                .build();

        when(psychologistRepository.findById(1L)).thenReturn(Optional.of(psychologist));
        when(userService.update(eq(1L), any())).thenReturn(psychologist.getIdUser());
        when(psychologistRepository.save(any(Psychologist.class))).thenReturn(psychologist);

        Psychologist result = psychologistService.updatePsychologist(1L, req);

        assertNotNull(result);
        assertEquals("54321", result.getCrp());
        verify(psychologistRepository).save(any(Psychologist.class));
    }

    @Test
    void testUpdatePsychologistNotFound() {
        PsychologistRequest req = PsychologistRequest.builder().build();
        when(psychologistRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> psychologistService.updatePsychologist(1L, req)
        );
        assertEquals("Psicólogo não encontrado com o ID: 1", ex.getMessage());
    }

    @Test
    void testDeletePsychologistSuccess() {
        when(psychologistRepository.findById(1L)).thenReturn(Optional.of(psychologist));
        doNothing().when(userService).delete(anyLong());

        psychologistService.delete(1L);

        verify(psychologistRepository).delete(psychologist);
        verify(userService).delete(1L);
    }

    @Test
    void testDeletePsychologistNotFound() {
        when(psychologistRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> psychologistService.delete(1L)
        );
        assertEquals("Psicólogo não encontrado com o ID: 1", ex.getMessage());
    }

    @Test
    void testGetPsychologistByIdSuccess() {
        when(psychologistRepository.findById(1L)).thenReturn(Optional.of(psychologist));

        Psychologist result = psychologistService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getIdPsychologist());
    }

    @Test
    void testGetByIdNotFound() {
        when(psychologistRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> psychologistService.getById(1L)
        );
        assertEquals("Psicólogo não encontrado com o ID: 1", ex.getMessage());
    }

    @Test
    void testListPsychologistsNonEmpty() {
        Page<Psychologist> page = new PageImpl<>(java.util.List.of(psychologist));
        when(psychologistRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Psychologist> result = psychologistService.list(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testListPsychologistsEmpty() {
        Page<Psychologist> page = new PageImpl<>(java.util.List.of());
        when(psychologistRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Psychologist> result = psychologistService.list(PageRequest.of(0, 10));

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
    }
}

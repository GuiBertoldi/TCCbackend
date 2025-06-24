package com.tcc.backend;

import com.tcc.backend.dtos.user.UserRequest;
import com.tcc.backend.enums.UserType;
import com.tcc.backend.models.User;
import com.tcc.backend.repositories.UserRepository;
import com.tcc.backend.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @InjectMocks private UserService  userService;

    private final String encoded = new BCryptPasswordEncoder().encode("secret");
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setIdUser(1L);
        user.setCpf("12345678900");
        user.setEmail("user@example.com");
        user.setPassword(encoded);
    }

    @Test
    void testValidatePassword_BcryptMatch() {
        assertTrue(userService.validatePassword("secret", encoded));
    }

    @Test
    void testValidatePassword_PlainMatch() {
        String raw = "plainValue";
        assertTrue(userService.validatePassword(raw, raw));
    }

    @Test
    void testValidatePassword_Failure() {
        assertFalse(userService.validatePassword("wrong", encoded));
    }

    @Test
    void testGetById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.getById(1L)
        );
        assertEquals("Usuário não encontrado.", ex.getMessage());
    }

    @Test
    void testGetById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        assertEquals(1L, userService.getById(1L).getIdUser());
    }

    @Test
    void testGetByEmail_NotFound() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.empty());
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.getByEmail("user@example.com")
        );
        assertEquals("Usuário com email não encontrado.", ex.getMessage());
    }

    @Test
    void testGetByEmail_Success() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        assertEquals("user@example.com", userService.getByEmail("user@example.com").getEmail());
    }

    @Test
    void testGetByCpf_NotFound() {
        when(userRepository.findByCpf("12345678900")).thenReturn(Optional.empty());
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.getByCpf("12345678900")
        );
        assertEquals("Usuário com CPF não encontrado.", ex.getMessage());
    }

    @Test
    void testGetByCpf_Success() {
        when(userRepository.findByCpf("12345678900")).thenReturn(Optional.of(user));
        assertEquals("12345678900", userService.getByCpf("12345678900").getCpf());
    }

    @Test
    void testCreate_Psicologo_Success() {
        UserRequest req = new UserRequest();
        req.setType(UserType.PSICOLOGO);
        req.setEmail("new@psych.com");
        req.setPassword("pwd123");

        User saved = new User();
        saved.setEmail("new@psych.com");
        when(userRepository.save(any())).thenReturn(saved);

        User out = userService.create(req);
        assertEquals("new@psych.com", out.getEmail());
        verify(userRepository).save(argThat(u -> u.getPassword() != null && !u.getPassword().isEmpty()));
    }

    @Test
    void testCreate_Patient_Success() {
        UserRequest req = new UserRequest();
        req.setType(UserType.PACIENTE);
        req.setEmail("new@patient.com");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User out = userService.create(req);

        assertEquals("new@patient.com", out.getEmail());
        verify(userRepository).save(argThat(u -> u.getPassword() == null));
    }

    @Test
    void testCreate_PasswordNullForNonPaciente() {
        UserRequest req = new UserRequest();
        req.setType(UserType.PSICOLOGO);
        req.setPassword("");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.create(req)
        );
        assertEquals("Senha não pode ser nula para este tipo de usuário.", ex.getMessage());
    }

    @Test
    void testUpdate_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.update(1L, new UserRequest())
        );
        assertEquals("Usuário não encontrado.", ex.getMessage());
    }

    @Test
    void testUpdate_Paciente_KeepsPassword() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserRequest req = new UserRequest();
        req.setType(UserType.PACIENTE);
        req.setPassword(null);

        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        User out = userService.update(1L, req);
        assertEquals(encoded, out.getPassword());
    }

    @Test
    void testUpdate_NonPaciente_NullPassword() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserRequest req = new UserRequest();
        req.setType(UserType.PSICOLOGO);
        req.setPassword(null);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.update(1L, req)
        );
        assertEquals("Senha não pode ser nula para este tipo de usuário.", ex.getMessage());
    }

    @Test
    void testDelete_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        assertDoesNotThrow(() -> userService.delete(1L));
        verify(userRepository).delete(user);
    }

    @Test
    void testDelete_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.delete(1L)
        );
        assertEquals("Usuário não encontrado.", ex.getMessage());
    }

    @Test
    void testGetUsersByType() {
        User u1 = new User(); u1.setType(UserType.PSICOLOGO);
        User u2 = new User(); u2.setType(UserType.PSICOLOGO);
        when(userRepository.findByType(UserType.PSICOLOGO)).thenReturn(Arrays.asList(u1, u2));

        List<User> out = userService.getUsersByType(UserType.PSICOLOGO);
        assertEquals(2, out.size());
    }

    @Test
    void testList_NoFilter() {
        PageRequest page = PageRequest.of(0, 5);
        Page<User> p = new PageImpl<>(Collections.singletonList(user), page, 1);
        when(userRepository.findAll(page)).thenReturn(p);

        assertEquals(1, userService.list("", page).getTotalElements());
    }

    @Test
    void testList_WithFilter() {
        PageRequest page = PageRequest.of(0, 5);
        Page<User> p = new PageImpl<>(Collections.singletonList(user), page, 1);
        when(userRepository.findByNameContainingIgnoreCase("use", page)).thenReturn(p);

        assertEquals(1, userService.list("use", page).getTotalElements());
    }
}

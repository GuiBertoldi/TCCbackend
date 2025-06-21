package com.tcc.backend;

import com.tcc.backend.dtos.user.UserRequest;
import com.tcc.backend.enums.UserType;
import com.tcc.backend.models.User;
import com.tcc.backend.repositories.UserRepository;
import com.tcc.backend.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setIdUser(1L);
        user.setCpf("12345678900");
        user.setEmail("user@example.com");
        user.setPassword(passwordEncoder.encode("myPassword123"));
    }

    @Test
    public void testValidatePasswordSuccess() {
        assertTrue(userService.validatePassword("myPassword123", user.getPassword()));
    }

    @Test
    public void testValidatePasswordFailure() {
        assertFalse(userService.validatePassword("wrongPassword", user.getPassword()));
    }

    @Test
    public void testGetByIdNotFound() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> userService.getById(1L));
        assertEquals("Usuário não encontrado.", ex.getMessage());
    }

    @Test
    public void testGetByIdSuccess() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User fetched = userService.getById(1L);
        assertEquals(1L, fetched.getIdUser());
    }

    @Test
    public void testGetByEmailNotFound() {
        Mockito.when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.empty());
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> userService.getByEmail("user@example.com"));
        assertEquals("Usuário com email não encontrado.", ex.getMessage());
    }

    @Test
    public void testGetByCpfNotFound() {
        Mockito.when(userRepository.findByCpf("12345678900")).thenReturn(Optional.empty());
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> userService.getByCpf("12345678900"));
        assertEquals("Usuário com CPF não encontrado.", ex.getMessage());
    }

    @Test
    public void testGetByCpfSuccess() {
        Mockito.when(userRepository.findByCpf("12345678900")).thenReturn(Optional.of(user));
        User fetched = userService.getByCpf("12345678900");
        assertEquals("12345678900", fetched.getCpf());
    }

    @Test
    public void testCreateUserSuccess() {
        UserRequest req = new UserRequest();
        req.setType(UserType.PSICOLOGO);
        req.setEmail("newUser@example.com");
        req.setPassword("newPassword123");

        User savedMock = new User();
        savedMock.setEmail(req.getEmail());
        savedMock.setPassword(passwordEncoder.encode(req.getPassword()));

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(savedMock);

        User result = userService.create(req);
        assertNotNull(result);
        assertEquals("newUser@example.com", result.getEmail());
    }

    @Test
    public void testUpdateUserSuccess() {
        UserRequest req = new UserRequest();
        req.setType(UserType.PACIENTE);
        req.setName("Updated User");
        req.setEmail("updatedEmail@example.com");
        req.setPassword(null);

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User updatedMock = new User();
        updatedMock.setName(req.getName());
        updatedMock.setEmail(req.getEmail());
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(updatedMock);

        User result = userService.update(1L, req);
        assertEquals("updatedEmail@example.com", result.getEmail());
        assertEquals("Updated User", result.getName());
    }

    @Test
    public void testUpdateUserFailureWhenPasswordNullForNonPaciente() {
        UserRequest req = new UserRequest();
        req.setType(UserType.PSICOLOGO);
        req.setPassword(null);

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> userService.update(1L, req));
        assertEquals("Senha não pode ser nula para este tipo de usuário.", ex.getMessage());
    }

    @Test
    public void testDeleteUserSuccess() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        assertDoesNotThrow(() -> userService.delete(1L));
        Mockito.verify(userRepository).delete(user);
    }

    @Test
    public void testDeleteUserNotFound() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> userService.delete(1L));
        assertEquals("Usuário não encontrado.", ex.getMessage());
    }

    @Test
    public void testGetUsersByType() {
        User u1 = new User(); u1.setType(UserType.PSICOLOGO);
        User u2 = new User(); u2.setType(UserType.PSICOLOGO);
        List<User> list = Arrays.asList(u1, u2);
        Mockito.when(userRepository.findByType(UserType.PSICOLOGO)).thenReturn(list);

        List<User> result = userService.getUsersByType(UserType.PSICOLOGO);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(u -> u.getType() == UserType.PSICOLOGO));
    }

    @Test
    public void testListWithoutName() {
        PageRequest page = PageRequest.of(0, 10);
        Page<User> pageAll = new PageImpl<>(Collections.singletonList(user));
        Mockito.when(userRepository.findAll(page)).thenReturn(pageAll);

        Page<User> result = userService.list("", page);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testListWithNameFilter() {
        PageRequest page = PageRequest.of(0, 10);
        Page<User> pageFiltered = new PageImpl<>(Collections.singletonList(user));
        Mockito.when(userRepository.findByNameContainingIgnoreCase("use", page)).thenReturn(pageFiltered);

        Page<User> result = userService.list("use", page);
        assertEquals(1, result.getTotalElements());
    }
}

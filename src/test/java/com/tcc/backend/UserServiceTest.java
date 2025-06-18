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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
        user.setEmail("user@example.com");
        user.setPassword(passwordEncoder.encode("myPassword123"));
    }

    @Test
    public void testValidatePasswordSuccess() {
        String rawPassword = "myPassword123";
        assertTrue(userService.validatePassword(rawPassword, user.getPassword()));
    }

    @Test
    public void testValidatePasswordFailure() {
        String wrongPassword = "wrongPassword";
        assertFalse(userService.validatePassword(wrongPassword, user.getPassword()));
    }

    @Test
    public void testGetByIdNotFound() {
        Mockito.when(userRepository.findById(user.getIdUser()))
                .thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            userService.getById(user.getIdUser());
        });
        assertEquals("Usuário não encontrado.", ex.getMessage());
    }

    @Test
    public void testGetByIdSuccess() {
        Mockito.when(userRepository.findById(user.getIdUser()))
                .thenReturn(Optional.of(user));

        User fetched = userService.getById(user.getIdUser());
        assertNotNull(fetched);
        assertEquals(user.getIdUser(), fetched.getIdUser());
    }

    @Test
    public void testGetByEmailNotFound() {
        Mockito.when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            userService.getByEmail(user.getEmail());
        });
        assertEquals("Usuário com email não encontrado.", ex.getMessage());
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

        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(savedMock);

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

        Mockito.when(userRepository.findById(user.getIdUser()))
                .thenReturn(Optional.of(user));

        User updatedMock = new User();
        updatedMock.setName(req.getName());
        updatedMock.setEmail(req.getEmail());
        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(updatedMock);

        User result = userService.update(user.getIdUser(), req);
        assertNotNull(result);
        assertEquals("updatedEmail@example.com", result.getEmail());
        assertEquals("Updated User", result.getName());
    }

    @Test
    public void testUpdateUserFailureWhenPasswordNullForNonPaciente() {
        UserRequest req = new UserRequest();
        req.setType(UserType.PSICOLOGO);
        req.setPassword(null);

        Mockito.when(userRepository.findById(user.getIdUser()))
                .thenReturn(Optional.of(user));

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            userService.update(user.getIdUser(), req);
        });
        assertEquals("Senha não pode ser nula para este tipo de usuário.", ex.getMessage());
    }
}

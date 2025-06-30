package com.tcc.backend;

import com.tcc.backend.config.JwtUtil;
import com.tcc.backend.controllers.AuthController;
import com.tcc.backend.dtos.login.LoginRequest;
import com.tcc.backend.models.User;
import com.tcc.backend.enums.UserType;
import com.tcc.backend.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void testLoginWithValidCredentials() throws Exception {
        new LoginRequest("test@example.com", "validPassword");
        User user = new User(1L, UserType.PSICOLOGO, "Test User", "test@example.com", "hashedPassword", "12345678901", "123456789", "12345", "Test City", "Test Neighborhood", "Test Street", 123);

        when(userService.getByEmail("test@example.com")).thenReturn(user);
        when(userService.validatePassword("validPassword", "hashedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("1")).thenReturn("validToken");

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content("{ \"email\": \"test@example.com\", \"password\": \"validPassword\" }"))
                .andExpect(status().isOk());
    }

    @Test
    void testLoginWhenUserNotFound() throws Exception {
        new LoginRequest("test@example.com", "validPassword");

        when(userService.getByEmail("test@example.com")).thenReturn(null);

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content("{ \"email\": \"test@example.com\", \"password\": \"validPassword\" }"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testLoginWithInvalidCredentials() throws Exception {
        new LoginRequest("test@example.com", "invalidPassword");
        User user = new User(1L, UserType.PSICOLOGO, "Test User", "test@example.com", "hashedPassword", "12345678901", "123456789", "12345", "Test City", "Test Neighborhood", "Test Street", 123);

        when(userService.getByEmail("test@example.com")).thenReturn(user);
        when(userService.validatePassword("invalidPassword", "hashedPassword")).thenReturn(false);

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content("{ \"email\": \"test@example.com\", \"password\": \"invalidPassword\" }"))
                .andExpect(status().isUnauthorized());
    }
}

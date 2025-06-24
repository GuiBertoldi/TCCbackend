package com.tcc.backend;

import com.tcc.backend.config.JwtUtil;
import com.tcc.backend.controllers.AuthController;
import com.tcc.backend.dtos.login.LoginRequest;
import com.tcc.backend.models.User;
import com.tcc.backend.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    private User user;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();

        user = User.builder()
                .idUser(1L)
                .email("user@example.com")
                .password("password123")
                .build();

        loginRequest = new LoginRequest();
        loginRequest.setEmail("user@example.com");
        loginRequest.setPassword("password123");
    }

    @Test
    void testLoginWithValidCredentials() throws Exception {
        when(userService.getByEmail(anyString())).thenReturn(user);
        when(userService.validatePassword(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateToken(anyString())).thenReturn("valid-jwt-token");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"user@example.com\", \"password\": \"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("valid-jwt-token"));
    }

    @Test
    void testLoginWithInvalidCredentials() throws Exception {
        when(userService.getByEmail(anyString())).thenReturn(user);
        when(userService.validatePassword(anyString(), anyString())).thenReturn(false);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"user@example.com\", \"password\": \"wrongpassword\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Credenciais inválidas"));
    }

    @Test
    void testLoginWhenUserNotFound() throws Exception {
        when(userService.getByEmail(anyString())).thenReturn(null);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"user@example.com\", \"password\": \"password123\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Credenciais inválidas"));
    }
}

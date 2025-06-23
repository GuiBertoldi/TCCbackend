package com.tcc.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcc.backend.config.JwtRequestFilter;
import com.tcc.backend.config.JwtUtil;
import com.tcc.backend.controllers.SessionController;
import com.tcc.backend.dtos.session.SessionRequest;
import com.tcc.backend.models.Patient;
import com.tcc.backend.models.Psychologist;
import com.tcc.backend.models.Session;
import com.tcc.backend.services.SessionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SessionController.class)
@AutoConfigureMockMvc(addFilters = false)
class SessionControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private SessionService service;

    @MockBean JwtRequestFilter jwtRequestFilter;
    @MockBean JwtUtil jwtUtil;

    private SessionRequest buildRequest() {
        return SessionRequest.builder()
                .patientId(42L)
                .idUser(17L)
                .sessionDate(LocalDate.of(2025, 6, 23))
                .reason("Progress check")
                .description("Discussed coping strategies")
                .build();
    }

    private Session buildSession() {
        Patient p = Patient.builder().idPatient(42L).build();
        Psychologist psych = Psychologist.builder().idPsychologist(17L).build();
        return Session.builder()
                .idSession(1L)
                .idPatient(p)
                .idPsychologist(psych)
                .sessionDate(LocalDate.of(2025, 6, 23))
                .reason("Progress check")
                .description("Discussed coping strategies")
                .build();
    }

    @Test @DisplayName("POST /sessions → 201 + JSON")
    void create_returns201AndBody() throws Exception {
        Session s = buildSession();
        when(service.create(any(SessionRequest.class))).thenReturn(s);

        mvc.perform(post("/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(buildRequest())))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idSession").value(s.getIdSession()))
                .andExpect(jsonPath("$.description").value(s.getDescription()));
    }

    @Test @DisplayName("PUT /sessions/{id} → 200")
    void update_returns200() throws Exception {
        when(service.update(eq(1L), any(SessionRequest.class)))
                .thenReturn(buildSession());

        mvc.perform(put("/sessions/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(buildRequest())))
                .andExpect(status().isOk());
    }

    @Test @DisplayName("DELETE /sessions/{id} → 200")
    void delete_returns200() throws Exception {
        doNothing().when(service).delete(1L);

        mvc.perform(delete("/sessions/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test @DisplayName("GET /sessions/{id} → 200 + JSON")
    void getById_returns200AndBody() throws Exception {
        Session s = buildSession();
        when(service.getById(1L)).thenReturn(s);

        mvc.perform(get("/sessions/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idSession").value(s.getIdSession()))
                .andExpect(jsonPath("$.reason").value(s.getReason()));
    }

    @Test @DisplayName("GET /sessions/user/{userId} → 200 + JSON list")
    void getByUser_returns200AndList() throws Exception {
        Session s = buildSession();
        when(service.getSessionsByUserId(42L)).thenReturn(List.of(s));

        mvc.perform(get("/sessions/user/{userId}", 42L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].idSession").value(s.getIdSession()));
    }

    @Test @DisplayName("GET /sessions → 200 + paged JSON")
    void list_returns200AndPage() throws Exception {
        Session s = buildSession();
        Page<Session> page = new PageImpl<>(List.of(s), PageRequest.of(0, 10), 1);
        when(service.list(any(Pageable.class))).thenReturn(page);

        mvc.perform(get("/sessions")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].idSession").value(s.getIdSession()));
    }
}

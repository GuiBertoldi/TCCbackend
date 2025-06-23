package com.tcc.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcc.backend.config.JwtRequestFilter;
import com.tcc.backend.config.JwtUtil;
import com.tcc.backend.controllers.PsychologistController;
import com.tcc.backend.dtos.psychologist.PsychologistRequest;
import com.tcc.backend.enums.UserType;
import com.tcc.backend.models.Psychologist;
import com.tcc.backend.models.User;
import com.tcc.backend.services.PsychologistService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PsychologistController.class)
@AutoConfigureMockMvc(addFilters = false)
class PsychologistControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private PsychologistService service;

    // Não usamos segurança nos testes de controller
    @MockBean
    private JwtRequestFilter jwtRequestFilter;
    @MockBean
    private JwtUtil jwtUtil;

    private Psychologist buildPsychologist() {
        User user = User.builder()
                .idUser(1L)
                .type(UserType.PSICOLOGO)
                .name("Dra. Fulana")
                .email("fulana@exemplo.com")
                .password("senha123")
                .cpf("12345678901")
                .phone("11999990000")
                .cep("01234-567")
                .city("São Paulo")
                .neighborhood("Centro")
                .street("Rua A")
                .number(100)
                .complement("Apto 1")
                .build();

        return Psychologist.builder()
                .idPsychologist(1L)
                .crp("CRP-12345")
                .idUser(user)
                .build();
    }

    private PsychologistRequest buildRequest() {
        return PsychologistRequest.builder()
                .type(UserType.PSICOLOGO)
                .name("Dra. Fulana")
                .email("fulana@exemplo.com")
                .password("senha123")
                .cpf("12345678901")
                .phone("11999990000")
                .crp("CRP-12345")
                .build();
    }

    @Test @DisplayName("POST /psychologists → 201 + corpo JSON")
    void create_devolve201ECorpo() throws Exception {
        Psychologist p = buildPsychologist();
        when(service.createPsychologist(any(PsychologistRequest.class)))
                .thenReturn(p);

        mvc.perform(post("/psychologists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(buildRequest())))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idPsychologist").value(p.getIdPsychologist()))
                .andExpect(jsonPath("$.crp").value(p.getCrp()))
                .andExpect(jsonPath("$.idUser.idUser").value(p.getIdUser().getIdUser()));
    }

    @Test @DisplayName("PUT /psychologists/{id} → 200")
    void update_devolve200() throws Exception {
        Psychologist p = buildPsychologist();
        when(service.updatePsychologist(eq(1L), any(PsychologistRequest.class)))
                .thenReturn(p);

        mvc.perform(put("/psychologists/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(buildRequest())))
                .andExpect(status().isOk());
    }

    @Test @DisplayName("DELETE /psychologists/{id} → 200")
    void delete_devolve200() throws Exception {
        doNothing().when(service).delete(1L);

        mvc.perform(delete("/psychologists/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test @DisplayName("GET /psychologists/{id} → 200 + corpo JSON")
    void getById_devolve200EUsuario() throws Exception {
        Psychologist p = buildPsychologist();
        when(service.getById(p.getIdPsychologist())).thenReturn(p);

        mvc.perform(get("/psychologists/{id}", p.getIdPsychologist()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idPsychologist").value(p.getIdPsychologist()));
    }

    @Test @DisplayName("GET /psychologists → 200 + página JSON")
    void list_devolve200EPagina() throws Exception {
        Psychologist p = buildPsychologist();
        Page<Psychologist> page = new PageImpl<>(List.of(p), PageRequest.of(0, 10), 1);
        when(service.list(any(Pageable.class))).thenReturn(page);

        mvc.perform(get("/psychologists")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].idPsychologist").value(p.getIdPsychologist()));
    }
}

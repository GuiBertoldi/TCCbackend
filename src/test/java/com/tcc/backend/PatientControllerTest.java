package com.tcc.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcc.backend.config.JwtRequestFilter;
import com.tcc.backend.config.JwtUtil;
import com.tcc.backend.controllers.PatientController;
import com.tcc.backend.dtos.patient.PatientRequest;
import com.tcc.backend.dtos.user.UserRequest;
import com.tcc.backend.enums.UserType;
import com.tcc.backend.models.Patient;
import com.tcc.backend.models.User;
import com.tcc.backend.services.PatientService;
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

@WebMvcTest(controllers = PatientController.class)
@AutoConfigureMockMvc(addFilters = false)
class PatientControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private PatientService service;

    // Desativamos o filtro de segurança para testes de controller
    @MockBean private JwtRequestFilter jwtRequestFilter;
    @MockBean private JwtUtil jwtUtil;

    private Patient buildPatient() {
        // primeiro criamos um User para embutir no Patient
        User user = User.builder()
                .idUser(42L)
                .type(UserType.PACIENTE)
                .name("Sr. Paciente")
                .email("paciente@exemplo.com")
                .password("senhaPaciente")
                .cpf("99988877766")
                .phone("11911112222")
                .cep("01010-010")
                .city("Rio de Janeiro")
                .neighborhood("Copacabana")
                .street("Av. Atlântica")
                .number(1000)
                .complement("Bloco A")
                .build();

        return Patient.builder()
                .idPatient(7L)
                .idUser(user)
                .build();
    }

    private UserRequest buildRequest() {
        return PatientRequest.builder()
                .type(UserType.PACIENTE)
                .name("Sr. Paciente")
                .email("paciente@exemplo.com")
                .password("senhaPaciente")
                .cpf("99988877766")
                .phone("11911112222")
                .cep("01010-010")
                .city("Rio de Janeiro")
                .neighborhood("Copacabana")
                .street("Av. Atlântica")
                .number(1000)
                .complement("Bloco A")
                .build();
    }

    @Test @DisplayName("POST /patients → 201 + corpo JSON")
    void create_devolve201ECorpo() throws Exception {
        Patient p = buildPatient();
        when(service.createPatient(any(PatientRequest.class))).thenReturn(p);

        mvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(buildRequest())))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idPatient").value(p.getIdPatient()))
                .andExpect(jsonPath("$.idUser.idUser").value(p.getIdUser().getIdUser()));
    }

    @Test @DisplayName("PUT /patients/{id} → 200")
    void update_devolve200() throws Exception {
        Patient p = buildPatient();
        when(service.updatePatient(eq(7L), any(PatientRequest.class))).thenReturn(p);

        mvc.perform(put("/patients/{id}", 7L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(buildRequest())))
                .andExpect(status().isOk());
    }

    @Test @DisplayName("DELETE /patients/{id} → 200")
    void delete_devolve200() throws Exception {
        doNothing().when(service).delete(7L);

        mvc.perform(delete("/patients/{id}", 7L))
                .andExpect(status().isOk());
    }

    @Test @DisplayName("GET /patients/{id} → 200 + corpo JSON")
    void getById_devolve200EEntidade() throws Exception {
        Patient p = buildPatient();
        when(service.getById(p.getIdPatient())).thenReturn(p);

        mvc.perform(get("/patients/{id}", p.getIdPatient()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idPatient").value(p.getIdPatient()));
    }

    @Test @DisplayName("GET /patients/search/{idUser} → 200 + corpo JSON")
    void findByUserId_devolve200EEntidade() throws Exception {
        Patient p = buildPatient();
        when(service.findByUserId(p.getIdUser().getIdUser())).thenReturn(p);

        mvc.perform(get("/patients/search/{idUser}", p.getIdUser().getIdUser()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idPatient").value(p.getIdPatient()));
    }

    @Test @DisplayName("GET /patients → 200 + página JSON")
    void list_devolve200EPagina() throws Exception {
        Patient p = buildPatient();
        Page<Patient> page = new PageImpl<>(List.of(p), PageRequest.of(0, 5), 1);
        when(service.list(any(Pageable.class))).thenReturn(page);

        mvc.perform(get("/patients")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].idPatient").value(p.getIdPatient()));
    }
}

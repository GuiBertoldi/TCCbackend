package com.tcc.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcc.backend.config.JwtRequestFilter;
import com.tcc.backend.config.JwtUtil;
import com.tcc.backend.controllers.TreatmentController;
import com.tcc.backend.dtos.treatment.TreatmentRequest;
import com.tcc.backend.models.Patient;
import com.tcc.backend.models.Treatment;
import com.tcc.backend.services.TreatmentService;
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

@WebMvcTest(controllers = TreatmentController.class)
@AutoConfigureMockMvc(addFilters = false)
class TreatmentControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private TreatmentService service;

    @MockBean JwtRequestFilter jwtRequestFilter;
    @MockBean JwtUtil jwtUtil;

    private TreatmentRequest buildRequest() {
        return TreatmentRequest.builder()
                .patientId(42L)
                .medicine("Ibuprofen")
                .startTreatment(LocalDate.of(2025, 6, 23))
                .endTreatment(LocalDate.of(2025, 7, 23))
                .build();
    }

    private Treatment buildTreatment() {
        Patient p = Patient.builder()
                .idPatient(42L)
                .build();

        return Treatment.builder()
                .idTreatment(1L)
                .medicine("Ibuprofen")
                .startTreatment(LocalDate.of(2025, 6, 23))
                .endTreatment(LocalDate.of(2025, 7, 23))
                .idPatient(p)
                .build();
    }

    @Test
    @DisplayName("POST /treatments → 201 + JSON body")
    void create_returns201AndBody() throws Exception {
        Treatment t = buildTreatment();
        when(service.create(any(TreatmentRequest.class))).thenReturn(t);

        mvc.perform(post("/treatments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(buildRequest())))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idTreatment").value(t.getIdTreatment()))
                .andExpect(jsonPath("$.medicine").value(t.getMedicine()))
                .andExpect(jsonPath("$.idPatient.idPatient").value(t.getIdPatient().getIdPatient()));
    }

    @Test
    @DisplayName("PUT /treatments/{id} → 200")
    void update_returns200() throws Exception {
        Treatment updated = buildTreatment();
        when(service.update(eq(1L), any(TreatmentRequest.class)))
                .thenReturn(updated);

        mvc.perform(put("/treatments/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(buildRequest())))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /treatments/{id} → 200")
    void delete_returns200() throws Exception {
        doNothing().when(service).delete(1L);

        mvc.perform(delete("/treatments/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /treatments/{id} → 200 + JSON body")
    void getById_returns200AndBody() throws Exception {
        Treatment t = buildTreatment();
        when(service.getById(1L)).thenReturn(t);

        mvc.perform(get("/treatments/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idTreatment").value(t.getIdTreatment()))
                .andExpect(jsonPath("$.medicine").value(t.getMedicine()));
    }

    @Test
    @DisplayName("GET /treatments/patient/{patientId} → 200 + JSON list")
    void findByUserId_returns200AndList() throws Exception {
        Treatment t = buildTreatment();
        when(service.getTreatmentsByUserId(42L)).thenReturn(List.of(t));

        mvc.perform(get("/treatments/patient/{patientId}", 42L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].idTreatment").value(t.getIdTreatment()));
    }

    @Test
    @DisplayName("GET /treatments → 200 + paged JSON")
    void list_returns200AndPage() throws Exception {
        Treatment t = buildTreatment();
        Page<Treatment> page = new PageImpl<>(List.of(t), PageRequest.of(0, 10), 1);
        when(service.list(any(Pageable.class))).thenReturn(page);

        mvc.perform(get("/treatments")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].idTreatment").value(t.getIdTreatment()));
    }
}

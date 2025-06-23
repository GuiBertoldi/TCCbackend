package com.tcc.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcc.backend.config.JwtRequestFilter;
import com.tcc.backend.controllers.FollowupController;
import com.tcc.backend.models.Followup;
import com.tcc.backend.models.Patient;
import com.tcc.backend.services.FollowupService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = FollowupController.class)
@AutoConfigureMockMvc(addFilters = false)
class FollowupControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private FollowupService service;

    @MockBean
    private JwtRequestFilter jwtRequestFilter;

    private Followup buildFollowup() {
        return Followup.builder()
                .idFollowUp(1L)
                .professionalName("Dr. Test")
                .professionalSpecialty("Especialidade X")
                .idPatient(
                        Patient.builder()
                                .idPatient(42L)
                                .build()
                )
                .build();
    }

    @Test
    @DisplayName("GET /followups/{id} -> 200 e JSON com o acompanhamento")
    void getById_returns200AndBody() throws Exception {
        Followup f = buildFollowup();
        when(service.getById(1L)).thenReturn(f);

        mvc.perform(get("/followups/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.professionalName").value(f.getProfessionalName()))
                .andExpect(jsonPath("$.professionalSpecialty").value(f.getProfessionalSpecialty()));
    }

    @Test
    @DisplayName("GET /followups/patient/{patientId} -> 200 e lista de acompanhamentos")
    void getByPatient_returns200AndList() throws Exception {
        Followup f = buildFollowup();
        when(service.getFollowupsByUserId(42L)).thenReturn(List.of(f));

        mvc.perform(get("/followups/patient/{patientId}", 42L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].professionalName").value(f.getProfessionalName()))
                .andExpect(jsonPath("$[0].professionalSpecialty").value(f.getProfessionalSpecialty()));
    }

}

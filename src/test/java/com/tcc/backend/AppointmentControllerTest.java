package com.tcc.backend;

import com.tcc.backend.controllers.AppointmentController;
import com.tcc.backend.dtos.appointment.AppointmentRequest;
import com.tcc.backend.enums.AppointmentStatus;
import com.tcc.backend.models.Appointment;
import com.tcc.backend.models.Patient;
import com.tcc.backend.models.Psychologist;
import com.tcc.backend.services.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AppointmentControllerTest {

    @Mock
    private AppointmentService appointmentService;
    @InjectMocks
    private AppointmentController appointmentController;

    private MockMvc mockMvc;

    private Patient patient;
    private Psychologist psychologist;
    private Appointment existing;
    private LocalDate today;
    private LocalTime at10;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(appointmentController).build();

        patient = Patient.builder().idPatient(1L).build();
        psychologist = Psychologist.builder().idPsychologist(2L).build();
        today = LocalDate.now();
        at10 = LocalTime.of(10, 0);
        existing = Appointment.builder()
                .idAppointment(5L)
                .patient(patient)
                .psychologist(psychologist)
                .date(today)
                .time(at10)
                .duration(60)
                .status(AppointmentStatus.SCHEDULED)
                .notes("Existing")
                .build();
    }

    @Test
    void testCreateAppointmentSuccess() throws Exception {
        AppointmentRequest req = new AppointmentRequest();
        req.setIdPatient(1L);
        req.setIdPsychologist(2L);
        req.setDate(today);
        req.setTime(at10.plusHours(2));
        req.setDuration(30);
        req.setStatus(AppointmentStatus.SCHEDULED);
        req.setNotes("Test Appointment");

        when(appointmentService.create(any(AppointmentRequest.class))).thenReturn(existing);

        mockMvc.perform(post("/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"idPatient\": 1,\n" +
                                "    \"idPsychologist\": 2,\n" +
                                "    \"date\": \"" + today + "\",\n" +
                                "    \"time\": \"" + at10.plusHours(2) + "\",\n" +
                                "    \"duration\": 30,\n" +
                                "    \"status\": \"SCHEDULED\",\n" +
                                "    \"notes\": \"Test Appointment\"\n" +
                                "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idAppointment").value(existing.getIdAppointment()))
                .andExpect(jsonPath("$.notes").value(existing.getNotes()));
    }

    @Test
    void testDeleteAppointmentSuccess() throws Exception {
        doNothing().when(appointmentService).delete(5L); // Simula a execução do método delete sem retorno

        mockMvc.perform(delete("/appointments/{idAppointment}", 5L))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAppointmentByIdSuccess() throws Exception {
        when(appointmentService.getById(5L)).thenReturn(existing);

        mockMvc.perform(get("/appointments/{idAppointment}", 5L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idAppointment").value(existing.getIdAppointment()))
                .andExpect(jsonPath("$.notes").value(existing.getNotes()));
    }

    @Test
    void testGetAppointmentsByUserId() throws Exception {
        when(appointmentService.getAppointmentByUserId(1L)).thenReturn(List.of(existing));

        mockMvc.perform(get("/appointments/user/{userId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idAppointment").value(existing.getIdAppointment()))
                .andExpect(jsonPath("$[0].notes").value(existing.getNotes()));
    }

}

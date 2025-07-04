package com.tcc.backend.dtos.appointment;

import com.tcc.backend.enums.AppointmentStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentRequest {
    private Long idPatient;
    private Long idPsychologist;
    private LocalDate date;
    private LocalTime time;
    private Integer duration;
    private AppointmentStatus status;
    private String notes;
}

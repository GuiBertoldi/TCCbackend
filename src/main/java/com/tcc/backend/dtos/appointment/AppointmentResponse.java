package com.tcc.backend.dtos.appointment;

import java.time.LocalDate;
import java.time.LocalTime;

import com.tcc.backend.enums.AppointmentStatus;
import com.tcc.backend.models.Appointment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {
    private Long id;
    private Long idPatient;
    private Long idPsychologist;
    private LocalDate date;
    private LocalTime time;
    private Integer duration;
    private AppointmentStatus status;
    private String notes;

    public static AppointmentResponse fromEntity(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getId(),
                appointment.getPatient().getIdPatient(),
                appointment.getPsychologist().getIdPsychologist(),
                appointment.getDate(),
                appointment.getTime(),
                appointment.getDuration(),
                appointment.getStatus(),
                appointment.getNotes()
        );
    }
}

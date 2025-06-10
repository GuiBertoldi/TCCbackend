package com.tcc.backend.dtos.appointment;

import com.tcc.backend.enums.AppointmentStatus;
import com.tcc.backend.models.Appointment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {
    private Long idAppointment;
    private Long idPatient;
    private Long idPsychologist;
    private LocalDate date;
    private LocalTime time;
    private Integer duration;
    private AppointmentStatus status;
    private String notes;

    public static AppointmentResponse fromEntity(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getIdAppointment(),
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

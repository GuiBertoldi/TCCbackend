package com.tcc.backend.models;

import com.tcc.backend.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "appointments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_app")
    private Long idAppointment;

    @ManyToOne
    @JoinColumn(name = "id_patient", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "id_psychologist", nullable = false)
    private Psychologist psychologist;

    @Column(name = "date_app")
    private LocalDate date;

    @Column(name = "time_app")
    private LocalTime time;

    @Column(name = "duration_app")
    private Integer duration;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_app")
    private AppointmentStatus status;

    @Column(name = "notes_app", length = 1024)
    private String notes;
}

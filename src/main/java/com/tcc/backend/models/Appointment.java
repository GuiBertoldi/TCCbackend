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
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "psychologist_id", nullable = false)
    private Psychologist psychologist;

    private LocalDate date;

    private LocalTime time;

    private Integer duration;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @Column(length = 1024)
    private String notes;
}

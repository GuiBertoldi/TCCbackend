package com.tcc.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idSession;
    private Long sessionNumber;
    private LocalDate sessionDate;
    private String reason;
    private String description;

    @ManyToOne
    @JoinColumn(name = "idPatient")
    private Patient idpatient;

    @ManyToOne
    @JoinColumn(name = "idUser")
    private User idPsychologist;


}

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
    @Column(name = "id_ses")
    private Long idSession;
    @Column(name = "numb_ses")
    private Long sessionNumber;
    @Column(name = "date_ses")
    private LocalDate sessionDate;
    @Column(name = "reas_ses")
    private String reason;
    @Column(name = "desc_ses")
    private String description;

    @ManyToOne
    @JoinColumn(name = "id_Patient")
    private Patient idpatient;
    @ManyToOne
    @JoinColumn(name = "id_psychologist")
    private User idPsychologist;
}

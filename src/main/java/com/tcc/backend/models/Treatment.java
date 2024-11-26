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
@Table(name = "treatment")
public class Treatment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_tre")
    private Long idTreatment;

    @Column(name = "med_tre", nullable = false)
    private String medicine;

    @Column(name = "start_tre", nullable = false)
    private LocalDate startTreatment;

    @Column(name = "end_tre", nullable = true)
    private LocalDate endTreatment;

    @ManyToOne
    @JoinColumn(name = "id_patient", referencedColumnName = "id_patient", nullable = false)
    private Patient idPatient;
}

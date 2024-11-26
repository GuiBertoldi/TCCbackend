package com.tcc.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "follow_up")
public class Followup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_fol")
    private Long idFollowUp;

    @Column(name = "doc_name", nullable = false)
    private String professionalName;

    @Column(name = "pro_spec", nullable = false)
    private String professionalSpecialty;

    @ManyToOne
    @JoinColumn(name = "id_patient", referencedColumnName = "id_patient", nullable = false)
    private Patient idPatient;
}

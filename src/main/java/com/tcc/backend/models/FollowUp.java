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
@Table(name = "followUp")
public class FollowUp {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_fol")
    private Long idFollowUp;
    @Column(name = "doc_name")
    private String professionalName;
    @Column(name = "med_spec")
    private String pro_specialty;
    @Column(name = "pro_spec")
    private String professionalSpecialty;

    @ManyToOne
    @JoinColumn(name = "idPatient")
    private Patient idpatient;
}

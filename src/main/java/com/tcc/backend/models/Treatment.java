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
    @Column(name = "med")
    private String medicine;
    @Column(name = "start_tre")
    private LocalDate StartTreatment;
    @Column(name = "end_tre")
    private LocalDate EndTreatment;


}

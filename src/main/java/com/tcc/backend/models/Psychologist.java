package com.tcc.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "psychologist")
public class Psychologist{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_psychologist")
    private Long idPsychologist;
    @Column(name = "crp_user")
    private String crp;

    @OneToOne
    @JoinColumn(name = "idUser")
    private User idUser;


}

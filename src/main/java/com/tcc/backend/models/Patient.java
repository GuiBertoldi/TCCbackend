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
@Table(name = "patients")
public class Patient{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_patient")
    private Long idPatient;

    @OneToOne
    @JoinColumn(name = "id_User")
    private User idUser;

    @Column(name = "emer_con")
    private String emergencyContact;

    @Column(name = "fat_name")
    private String fatherName;
    @Column(name = "fat_edu")
    private String fatherEducation;
    @Column(name = "fat_age")
    private Integer fatherAge;
    @Column(name = "fat_work")
    private String fatherWorkplace;
    @Column(name = "fat_prof")
    private String fatherProfession;

    @Column(name = "mot_name")
    private String motherName;
    @Column(name = "mot_edu")
    private String motherEducation;
    @Column(name = "mot_age")
    private Integer motherAge;
    @Column(name = "mot_work")
    private String motherWorkplace;
    @Column(name = "mot_prof")
    private String motherProfession;
}

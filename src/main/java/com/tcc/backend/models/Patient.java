package com.tcc.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "patients")
public class Patient extends User{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_Patient")
    private Long idPatient;

    //extends user
    private String name;
    private String email;
    private String status;
    private String cpf;
    private String phone;

    @Column(name = "emer_con")
    private String emergencyContact;

    //extends user
    private String cep;
    private String city;
    private String neighborhood;
    private String street;
    private Integer number;
    private String complement;

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

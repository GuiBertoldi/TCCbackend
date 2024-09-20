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
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idPatient;
    private String name;
    private String email;
    private String status;
    private String cpf;
    private String phone;

    private String emergencyContact;
    private String cep;
    private String city;
    private String neighborhood;
    private String street;
    private Integer number;
    private String complement;

    private String fatherName;
    private String fatherEducation;
    private Integer fatherAge;
    private String fatherWorkplace;
    private String fatherProfession;

    private String motherName;
    private String motherEducation;
    private Integer motherAge;
    private String motherWorkplace;
    private String motherProfession;
}

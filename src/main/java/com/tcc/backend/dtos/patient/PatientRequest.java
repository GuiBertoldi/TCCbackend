package com.tcc.backend.dtos.patient;

import com.tcc.backend.dtos.user.UserRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientRequest extends UserRequest {

    private Long idPatient;
    private Long idUser;

    private String emergencyContact;

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


package com.tcc.backend.services;

import com.tcc.backend.dtos.patient.PatientRequest;
import com.tcc.backend.dtos.user.UserRequest;
import com.tcc.backend.enums.UserType;
import com.tcc.backend.models.Patient;
import com.tcc.backend.models.User;
import com.tcc.backend.repositories.PatientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PatientService {

    private final PatientRepository repository;
    private final UserService userService;

    @Autowired
    public PatientService(PatientRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public Patient createPatient(final PatientRequest patientRequest) {
        UserRequest userRequest = UserRequest.builder()
                .type(UserType.PACIENTE)
                .name(patientRequest.getName())
                .email(patientRequest.getEmail())
                .cpf(patientRequest.getCpf())
                .phone(patientRequest.getPhone())
                .cep(patientRequest.getCep())
                .city(patientRequest.getCity())
                .neighborhood(patientRequest.getNeighborhood())
                .street(patientRequest.getStreet())
                .number(patientRequest.getNumber())
                .build();

        User createdUser = userService.create(userRequest);

        Patient patient = Patient.builder()
                .idUser(createdUser)
                .emergencyContact(patientRequest.getEmergencyContact())
                .fatherName(patientRequest.getFatherName())
                .fatherEducation(patientRequest.getFatherEducation())
                .fatherAge(patientRequest.getFatherAge())
                .fatherWorkplace(patientRequest.getFatherWorkplace())
                .fatherProfession(patientRequest.getFatherProfession())
                .motherName(patientRequest.getMotherName())
                .motherEducation(patientRequest.getMotherEducation())
                .motherAge(patientRequest.getMotherAge())
                .motherWorkplace(patientRequest.getMotherWorkplace())
                .motherProfession(patientRequest.getMotherProfession())
                .build();

        return repository.save(patient);
    }

    public Patient updatePatient(Long idPatient, PatientRequest patientRequest) {
        Patient existingPatient = getById(idPatient);

        UserRequest userRequest = UserRequest.builder()
                .idUser(existingPatient.getIdUser().getIdUser())
                .type(UserType.PACIENTE)
                .name(patientRequest.getName())
                .email(patientRequest.getEmail())
                .cpf(patientRequest.getCpf())
                .phone(patientRequest.getPhone())
                .cep(patientRequest.getCep())
                .city(patientRequest.getCity())
                .neighborhood(patientRequest.getNeighborhood())
                .street(patientRequest.getStreet())
                .number(patientRequest.getNumber())
                .build();

        userService.update(existingPatient.getIdUser().getIdUser(), userRequest);

        existingPatient.setEmergencyContact(patientRequest.getEmergencyContact());
        existingPatient.setFatherName(patientRequest.getFatherName());
        existingPatient.setFatherEducation(patientRequest.getFatherEducation());
        existingPatient.setFatherAge(patientRequest.getFatherAge());
        existingPatient.setFatherWorkplace(patientRequest.getFatherWorkplace());
        existingPatient.setFatherProfession(patientRequest.getFatherProfession());
        existingPatient.setMotherName(patientRequest.getMotherName());
        existingPatient.setMotherEducation(patientRequest.getMotherEducation());
        existingPatient.setMotherAge(patientRequest.getMotherAge());
        existingPatient.setMotherWorkplace(patientRequest.getMotherWorkplace());
        existingPatient.setMotherProfession(patientRequest.getMotherProfession());

        return repository.save(existingPatient);
    }

    public void delete(Long idPatient) {
        Patient patient = repository.getById(idPatient);
        userService.delete(patient.getIdUser().getIdUser());
        repository.delete(patient);
    }

    public Patient getById(Long idPatient) {
        return repository.findById(idPatient).orElseThrow(() ->
                new IllegalArgumentException("Paciente não encontrado com o ID: " + idPatient));
    }

    public Patient findByUserId(Long idUser) {
        return repository.findByUserId(idUser).orElseThrow(() ->
                new IllegalArgumentException("Paciente não encontrado com o ID de usuário: " + idUser));
    }

    public Page<Patient> list(Pageable pageable) {
        return repository.findAll(pageable);
    }
}

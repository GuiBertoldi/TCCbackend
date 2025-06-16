package com.tcc.backend.services;

import com.tcc.backend.dtos.followup.FollowupRequest;
import com.tcc.backend.models.Followup;
import com.tcc.backend.models.Patient;
import com.tcc.backend.repositories.FollowupRepository;
import com.tcc.backend.repositories.PatientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class FollowupService {

    private final FollowupRepository followupRepository;
    private final PatientRepository patientRepository;

    @Autowired
    public FollowupService(FollowupRepository followupRepository, PatientRepository patientRepository) {
        this.followupRepository = followupRepository;
        this.patientRepository = patientRepository;
    }

    public Followup create(FollowupRequest request) {
        Patient patient = patientRepository.findByUserId(request.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado com o ID: " + request.getPatientId()));

        Followup followup = Followup.builder()
                .idPatient(patient)
                .professionalName(request.getProfessionalName())
                .professionalSpecialty(request.getProfessionalSpecialty())
                .build();

        return followupRepository.save(followup);
    }

    public Followup update(Long idFollowup, FollowupRequest request) {
        Followup existingFollowup = getById(idFollowup);

        Patient patient = patientRepository.findByUserId(request.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado com o ID: " + request.getPatientId()));

        existingFollowup.setIdPatient(patient);
        existingFollowup.setProfessionalName(request.getProfessionalName());
        existingFollowup.setProfessionalSpecialty(request.getProfessionalSpecialty());

        return followupRepository.save(existingFollowup);
    }

    public void delete(Long idFollowup) {
        Followup followup = getById(idFollowup);
        followupRepository.delete(followup);
    }

    public Followup getById(Long idFollowup) {
        return followupRepository.findById(idFollowup)
                .orElseThrow(() -> new IllegalArgumentException("Acompanhamento não encontrado com o ID: " + idFollowup));
    }

    public Followup getByPatient(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado com o ID: " + patientId));
        return followupRepository.findByIdPatient(patient)
                .orElseThrow(() -> new IllegalArgumentException("Acompanhamento não encontrado para o paciente informado."));
    }

    public List<Followup> getFollowupsByUserId(Long userId) {
        return followupRepository.findFollowupsByUserId(userId);
    }

    public Page<Followup> listFollowups(Pageable pageable) {
        return followupRepository.findAll(pageable);
    }
}

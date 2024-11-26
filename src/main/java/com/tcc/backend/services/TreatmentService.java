package com.tcc.backend.services;

import com.tcc.backend.dtos.treatment.TreatmentRequest;
import com.tcc.backend.models.Patient;
import com.tcc.backend.models.Treatment;
import com.tcc.backend.repositories.PatientRepository;
import com.tcc.backend.repositories.TreatmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TreatmentService {

    private final TreatmentRepository treatmentRepository;
    private final PatientRepository patientRepository;

    @Autowired
    public TreatmentService(TreatmentRepository treatmentRepository, PatientRepository patientRepository) {
        this.treatmentRepository = treatmentRepository;
        this.patientRepository = patientRepository;
    }

    public Treatment create(TreatmentRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado com o ID: " + request.getPatientId()));

        Treatment treatment = Treatment.builder()
                .idPatient(patient)
                .medicine(request.getMedicine())
                .startTreatment(request.getStartTreatment())
                .endTreatment(request.getEndTreatment())
                .build();

        return treatmentRepository.save(treatment);
    }

    public Treatment update(Long idTreatment, TreatmentRequest request) {
        Treatment existingTreatment = getById(idTreatment);

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado com o ID: " + request.getPatientId()));

        existingTreatment.setIdPatient(patient);
        existingTreatment.setMedicine(request.getMedicine());
        existingTreatment.setStartTreatment(request.getStartTreatment());
        existingTreatment.setEndTreatment(request.getEndTreatment());

        return treatmentRepository.save(existingTreatment);
    }

    public void delete(Long idTreatment) {
        Treatment treatment = getById(idTreatment);
        treatmentRepository.delete(treatment);
    }

    public Treatment getById(Long idTreatment) {
        return treatmentRepository.findById(idTreatment)
                .orElseThrow(() -> new IllegalArgumentException("Tratamento não encontrado com o ID: " + idTreatment));
    }

    public Page<Treatment> list(Pageable pageable) {
        return treatmentRepository.findAll(pageable);
    }
}

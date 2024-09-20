package com.tcc.backend.services;

import com.tcc.backend.models.Patient;
import com.tcc.backend.repositories.PatientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

@Service
@Transactional
public class PatientService {

    @Autowired
    public PatientService(final PatientRepository repository) {
        this.repository = repository;
    }

    private final PatientRepository repository;

    public Patient create(final Patient patient) {
        Assert.isTrue(this.getByCpf(patient.getCpf()).isEmpty(), "Já existe um paciente cadastrado com este CPF.");
        final Patient newPatient = repository.save(patient);
        return newPatient;
    }

    public Patient update(final Patient patient) {
        Assert.notNull(patient.getIdPatient(), "Id não informado");
        Assert.isTrue(this.getById(patient.getIdPatient()).isPresent(), "Paciente não encontrado");
        return repository.save(patient);
    }

    public Optional<Patient> getById(final Long id) {
        return repository.findById(id);
    }

    public Optional<Patient> getByCpf(final String cpf) {
        return repository.findByCpf(cpf);
    }

    public Optional<Patient> getByName(final String name) {
        return repository.findByName(name);
    }

    public Page<Patient> list(Pageable pageable) {
        return repository.findAll(pageable);
    }
}

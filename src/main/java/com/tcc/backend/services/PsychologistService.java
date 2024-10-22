package com.tcc.backend.services;

import com.tcc.backend.models.Psychologist;
import com.tcc.backend.repositories.PsychologistRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

@Service
@Transactional
public class PsychologistService {

    @Autowired
    public PsychologistService(final PsychologistRepository repository) {
        this.repository = repository;
    }

    private final PsychologistRepository repository;

    public Psychologist create(final Psychologist psychologist) {
        Assert.isTrue(this.getByCpf(psychologist.getCpf()).isEmpty(), "Já existe um usuário cadastrado com este CPF.");
        final Psychologist newPsychologist = repository.save(psychologist);
        return newPsychologist;
    }

    public Psychologist update(final Psychologist psychologist) {
        Assert.notNull(psychologist.getIdUser(), "Id não informado");
        Assert.isTrue(this.getById(psychologist.getIdUser()).isPresent(), "Usuário não encontrado");
        return repository.save(psychologist);
    }

    public Optional<Psychologist> getById(final Long id) {
        return repository.findById(id);
    }

    public Optional<Psychologist> getByCpf(String cpf) {
        return repository.findByCpf(cpf);
    }

    public Page<Psychologist> list(Pageable pageable) {
        return repository.findAll(pageable);
    }
}

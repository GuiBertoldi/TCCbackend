package com.tcc.backend.services;

import com.tcc.backend.models.FollowUp;
import com.tcc.backend.models.Patient;
import com.tcc.backend.repositories.FollowUpRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

@Service
@Transactional
public class FollowUpService {

    private final FollowUpRepository repository;

    @Autowired
    public FollowUpService(FollowUpRepository repository) {
        this.repository = repository;
    }

    public FollowUp create(final FollowUp followUp) {
        final FollowUp newFollowUp = repository.save(followUp);
        return newFollowUp;
    }

    public FollowUp update(final FollowUp followUp) {
        return repository.save(followUp);
    }

    public Optional<FollowUp> findById(final Long idFollowUp) {
        return repository.findById(idFollowUp);
    }

    public Optional<FollowUp> findByIdPatient(final Patient IdPatient) {
        return repository.findByIdpatient(IdPatient);
    }

    public Page<FollowUp> list(Pageable pageable) {
        return repository.findAll(pageable);
    }
}

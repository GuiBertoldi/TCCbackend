package com.tcc.backend.services;

import com.tcc.backend.models.Treatment;
import com.tcc.backend.repositories.TreatmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

@Service
@Transactional
public class TreatmentService {

    @Autowired
    public TreatmentService(TreatmentRepository repository) {
        this.repository = repository;
    }

    private final TreatmentRepository repository;

    public Treatment create(final Treatment treatment){
        final Treatment newTreatment = repository.save(treatment);
        return newTreatment;
    }

    public Treatment update(final Treatment treatment){
        return repository.save(treatment);
    }

    public Optional<Treatment> findById(final Long idTreatment){
        return repository.findById(idTreatment);
    }
    public Page<Treatment> list(Pageable pageable) {
        return repository.findAll(pageable);
    }
}

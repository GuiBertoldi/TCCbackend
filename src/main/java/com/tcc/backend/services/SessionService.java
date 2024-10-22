package com.tcc.backend.services;

import com.tcc.backend.models.Patient;
import com.tcc.backend.models.Session;
import com.tcc.backend.models.User;
import com.tcc.backend.repositories.SessionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional
public class SessionService {

    @Autowired
    public SessionService(final SessionRepository repository, SessionRepository sessionRepository) {
        this.repository = repository;
    }

    private final SessionRepository repository;;

    public Session create(final Session session) {
        Patient patient = session.getIdpatient();
        Optional<Session> lastSession = repository.findTopByPatientOrderBySessionNumberDesc(patient.getIdUser());
        Long newSessionNumber = lastSession.map(last -> last.getSessionNumber() + 1).orElse(1L);
        session.setSessionNumber(newSessionNumber);

        final Session newSession = repository.save(session);
        return newSession;
    }

    public Session update(final Session session) {
        Assert.notNull(session.getIdSession(), "Id não informado");
        Assert.isTrue(this.getById(session.getIdSession()).isPresent(), "Sessão não encontrada");
        return repository.save(session);
    }

    public Optional<Session> getById(final Long id) {
        return repository.findById(id);
    }

    public Optional<Session> getByNamePsychologist(String namePsychologist) {
        return repository.findByNamePsychologist(namePsychologist);
    }

    public Optional<Session> getByNamePatient(final String namePatient) {
        return repository.findByNamePatient(namePatient);
    }

    public Optional<Session> getBySessionDate(final LocalDate sessionDate) {
        return repository.findBySessionDate(sessionDate);
    }

    public Optional<Session> getBySessionNumber(final Long sessionNumber) {
        return repository.findBySessionNumber(sessionNumber);
    }

    public Page<Session> list(Pageable pageable) {
        return repository.findAll(pageable);
    }
}

package com.tcc.backend.services;

import com.tcc.backend.dtos.session.SessionRequest;
import com.tcc.backend.models.Patient;
import com.tcc.backend.models.Psychologist;
import com.tcc.backend.models.Session;
import com.tcc.backend.repositories.PatientRepository;
import com.tcc.backend.repositories.PsychologistRepository;
import com.tcc.backend.repositories.SessionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class SessionService {

    private final SessionRepository sessionRepository;
    private final PatientRepository patientRepository;
    private final PsychologistRepository psychologistRepository;

    @Autowired
    public SessionService(SessionRepository sessionRepository, PatientRepository patientRepository, PsychologistRepository psychologistRepository) {
        this.sessionRepository = sessionRepository;
        this.patientRepository = patientRepository;
        this.psychologistRepository = psychologistRepository;
    }

    public Session create(SessionRequest sessionRequest) {
        Patient patient = patientRepository.findById(sessionRequest.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado com o ID: " + sessionRequest.getPatientId()));

        Psychologist psychologist = psychologistRepository.findByIdUserId(sessionRequest.getIdUser())
                .orElseThrow(() -> new IllegalArgumentException("Psicólogo não encontrado com o ID: " + sessionRequest.getIdUser()));

        long sessionNumber = sessionRepository.countByIdPatient(patient) + 1;

        Session session = Session.builder()
                .idPatient(patient)
                .idPsychologist(psychologist)
                .sessionNumber(sessionNumber)
                .sessionDate(sessionRequest.getSessionDate())
                .reason(sessionRequest.getReason())
                .description(sessionRequest.getDescription())
                .build();

        return sessionRepository.save(session);
    }


    public Session update(Long idSession, SessionRequest sessionRequest) {
        Session existingSession = getById(idSession);

        Patient patient = patientRepository.findById(sessionRequest.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado com o ID: " + sessionRequest.getPatientId()));

        Psychologist psychologist = psychologistRepository.findById(sessionRequest.getIdUser())
                .orElseThrow(() -> new IllegalArgumentException("Psicólogo não encontrado com o ID: " + sessionRequest.getIdUser()));

        existingSession.setSessionDate(sessionRequest.getSessionDate());
        existingSession.setReason(sessionRequest.getReason());
        existingSession.setDescription(sessionRequest.getDescription());
        existingSession.setIdPatient(patient);
        existingSession.setIdPsychologist(psychologist);

        return sessionRepository.save(existingSession);
    }

    public void delete(Long idSession) {
        Session session = sessionRepository.findById(idSession)
                .orElseThrow(() -> new IllegalArgumentException("Sessão não encontrada com o ID: " + idSession));
        sessionRepository.delete(session);
    }

    public Session getById(Long idSession) {
        return sessionRepository.findById(idSession)
                .orElseThrow(() -> new IllegalArgumentException("Sessão não encontrada com o ID: " + idSession));
    }

    public List<Session> getSessionsByUserId(Long userId) {
        return sessionRepository.findSessionsByUserId(userId);
    }

    public Page<Session> list(Pageable pageable) {
        return sessionRepository.findAll(pageable);
    }
}

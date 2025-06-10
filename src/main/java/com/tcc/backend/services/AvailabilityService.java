// AvailabilityService.java
package com.tcc.backend.services;

import com.tcc.backend.dtos.availability.AvailabilityRequest;
import com.tcc.backend.dtos.availability.AvailabilityResponse;
import com.tcc.backend.models.Psychologist;
import com.tcc.backend.models.PsychologistAvailability;
import com.tcc.backend.repositories.AvailabilityRepository;
import com.tcc.backend.repositories.PsychologistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvailabilityService {

    private final AvailabilityRepository repository;
    private final PsychologistRepository psychologistRepository;

    @Autowired
    public AvailabilityService(AvailabilityRepository repository, PsychologistRepository psychologistRepository) {
        this.repository = repository;
        this.psychologistRepository = psychologistRepository;
    }

    public AvailabilityResponse create(AvailabilityRequest request) throws Exception {
        Psychologist p = psychologistRepository.findById(request.getIdPsychologist())
                .orElseThrow(() -> new Exception("Psicólogo não encontrado"));
        PsychologistAvailability a = PsychologistAvailability.builder()
                .psychologist(p)
                .dayOfWeek(request.getDayOfWeek())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build();
        return AvailabilityResponse.fromEntity(repository.save(a));
    }

    public AvailabilityResponse update(Long id, AvailabilityRequest request) throws Exception {
        PsychologistAvailability a = repository.findById(id)
                .orElseThrow(() -> new Exception("Disponibilidade não encontrada"));
        a.setDayOfWeek(request.getDayOfWeek());
        a.setStartTime(request.getStartTime());
        a.setEndTime(request.getEndTime());
        return AvailabilityResponse.fromEntity(repository.save(a));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<AvailabilityResponse> listByPsychologist(Long idPsychologist) {
        return repository.findByPsychologistIdPsychologist(idPsychologist)
                .stream()
                .map(AvailabilityResponse::fromEntity)
                .collect(Collectors.toList());
    }
}

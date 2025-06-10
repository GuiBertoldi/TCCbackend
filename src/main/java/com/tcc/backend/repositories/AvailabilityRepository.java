package com.tcc.backend.repositories;

import com.tcc.backend.models.PsychologistAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface AvailabilityRepository extends JpaRepository<PsychologistAvailability, Long> {
    List<PsychologistAvailability> findByPsychologistIdPsychologist(Long idPsychologist);
    List<PsychologistAvailability> findByPsychologistIdPsychologistAndDayOfWeek(Long idPsychologist, DayOfWeek dayOfWeek);
}

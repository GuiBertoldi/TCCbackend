package com.tcc.backend.dtos.availability;

import com.tcc.backend.models.PsychologistAvailability;
import lombok.Data;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
public class AvailabilityResponse {
    private Long id;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    public static AvailabilityResponse fromEntity(PsychologistAvailability a) {
        AvailabilityResponse r = new AvailabilityResponse();
        r.setId(a.getId());
        r.setDayOfWeek(a.getDayOfWeek());
        r.setStartTime(a.getStartTime());
        r.setEndTime(a.getEndTime());
        return r;
    }
}

package com.tcc.backend.dtos.availability;

import lombok.Data;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
public class AvailabilityRequest {
    private Long idPsychologist;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
}

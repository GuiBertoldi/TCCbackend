package com.tcc.backend;

import com.tcc.backend.dtos.availability.AvailabilityRequest;
import com.tcc.backend.dtos.availability.AvailabilityResponse;
import com.tcc.backend.models.Psychologist;
import com.tcc.backend.models.PsychologistAvailability;
import com.tcc.backend.repositories.AvailabilityRepository;
import com.tcc.backend.repositories.PsychologistRepository;
import com.tcc.backend.services.AvailabilityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AvailabilityServiceTest {

    @Mock
    private AvailabilityRepository availabilityRepository;

    @Mock
    private PsychologistRepository psychologistRepository;

    @InjectMocks
    private AvailabilityService availabilityService;

    private Psychologist psych;
    private PsychologistAvailability avail;

    @BeforeEach
    void setUp() {
        psych = Psychologist.builder()
                .idPsychologist(42L)
                .build();

        avail = PsychologistAvailability.builder()
                .id(7L)
                .psychologist(psych)
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(17, 0))
                .build();
    }

    @Test
    void testCreateSuccess() throws Exception {
        AvailabilityRequest req = new AvailabilityRequest();
        req.setIdPsychologist(42L);
        req.setDayOfWeek(DayOfWeek.MONDAY);
        req.setStartTime(LocalTime.of(9, 0));
        req.setEndTime(LocalTime.of(17, 0));

        when(psychologistRepository.findById(42L)).thenReturn(Optional.of(psych));
        ArgumentCaptor<PsychologistAvailability> capt = ArgumentCaptor.forClass(PsychologistAvailability.class);
        when(availabilityRepository.save(capt.capture())).thenReturn(avail);

        AvailabilityResponse resp = availabilityService.create(req);

        PsychologistAvailability passed = capt.getValue();
        assertEquals(psych, passed.getPsychologist());
        assertEquals(DayOfWeek.MONDAY, passed.getDayOfWeek());
        assertEquals(LocalTime.of(9, 0), passed.getStartTime());
        assertEquals(LocalTime.of(17, 0), passed.getEndTime());

        assertEquals(7L, resp.getId());
        assertEquals(DayOfWeek.MONDAY, resp.getDayOfWeek());
        assertEquals(LocalTime.of(9, 0), resp.getStartTime());
        assertEquals(LocalTime.of(17, 0), resp.getEndTime());
    }

    @Test
    void testCreatePsychologistNotFound() {
        AvailabilityRequest req = new AvailabilityRequest();
        req.setIdPsychologist(99L);

        when(psychologistRepository.findById(99L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(Exception.class, () -> availabilityService.create(req));
        assertEquals("Psicólogo não encontrado", ex.getMessage());
    }

    @Test
    void testUpdateSuccess() throws Exception {
        AvailabilityRequest req = new AvailabilityRequest();
        req.setDayOfWeek(DayOfWeek.FRIDAY);
        req.setStartTime(LocalTime.of(10, 0));
        req.setEndTime(LocalTime.of(12, 0));

        when(availabilityRepository.findById(7L)).thenReturn(Optional.of(avail));
        when(availabilityRepository.save(avail)).thenReturn(avail);

        AvailabilityResponse resp = availabilityService.update(7L, req);

        assertEquals(7L, resp.getId());
        assertEquals(DayOfWeek.FRIDAY, resp.getDayOfWeek());
        assertEquals(LocalTime.of(10, 0), resp.getStartTime());
        assertEquals(LocalTime.of(12, 0), resp.getEndTime());
    }

    @Test
    void testUpdateNotFound() {
        AvailabilityRequest req = new AvailabilityRequest();

        when(availabilityRepository.findById(5L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(Exception.class, () -> availabilityService.update(5L, req));
        assertEquals("Disponibilidade não encontrada", ex.getMessage());
    }

    @Test
    void testDelete() {
        // no exception expected
        availabilityService.delete(123L);
        verify(availabilityRepository).deleteById(123L);
    }

    @Test
    void testListByPsychologist() {
        when(availabilityRepository.findByPsychologistIdPsychologist(42L))
                .thenReturn(List.of(avail));

        List<AvailabilityResponse> list = availabilityService.listByPsychologist(42L);

        assertEquals(1, list.size());
        AvailabilityResponse r = list.get(0);
        assertEquals(7L, r.getId());
        assertEquals(DayOfWeek.MONDAY, r.getDayOfWeek());
        assertEquals(LocalTime.of(9, 0), r.getStartTime());
        assertEquals(LocalTime.of(17, 0), r.getEndTime());
    }
}

package com.tcc.backend.controllers;

import com.tcc.backend.dtos.appointment.AppointmentRequest;
import com.tcc.backend.models.Appointment;
import com.tcc.backend.services.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService service;

    @GetMapping
    public List<Appointment> list(
            @RequestParam(required = false) Long psychologistId,
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        if (psychologistId != null && from != null && to != null) {
            return service.findByPsychologistAndPeriod(psychologistId, from, to);
        } else if (patientId != null) {
            return service.findByPatient(patientId);
        } else {
            return service.findAll();
        }
    }

    @GetMapping("/{id}")
    public Appointment get(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public Appointment create(@RequestBody AppointmentRequest appointmentRequest) {
        return service.create(appointmentRequest);
    }

    @PutMapping("/{id}")
    public Appointment update(@PathVariable Long id, @RequestBody AppointmentRequest appointmentRequest) {
        return service.update(id, appointmentRequest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
package com.tcc.backend.controllers;

import com.tcc.backend.dtos.appointment.AppointmentRequest;
import com.tcc.backend.dtos.appointment.AppointmentResponse;
import com.tcc.backend.models.Appointment;
import com.tcc.backend.services.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService service;

    @Autowired
    public AppointmentController(AppointmentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AppointmentResponse> create(@RequestBody @Valid AppointmentRequest request) throws Exception {
        Appointment appointment = service.create(request);
        return new ResponseEntity<>(AppointmentResponse.fromEntity(appointment), HttpStatus.CREATED);
    }

}
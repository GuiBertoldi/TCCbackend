package com.tcc.backend.controllers;

import com.tcc.backend.dtos.appointment.AppointmentRequest;
import com.tcc.backend.dtos.appointment.AppointmentResponse;
import com.tcc.backend.dtos.appointment.AppointmentUpdateRequest;
import com.tcc.backend.models.Appointment;
import com.tcc.backend.services.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
 
    @PutMapping("/{idAppointment}")
    public ResponseEntity<AppointmentResponse> update(@PathVariable Long idAppointment, @RequestBody @Valid AppointmentUpdateRequest request) throws Exception {
        Appointment appointment = service.update(idAppointment, request);
        return new ResponseEntity<>(AppointmentResponse.fromEntity(appointment), HttpStatus.OK);
    }

    @DeleteMapping("/{idAppointment}")
    public ResponseEntity<AppointmentResponse> delete(@PathVariable Long idAppointment) {
        service.delete(idAppointment);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{idAppointment}")
    public ResponseEntity<Appointment> getById(@PathVariable Long idAppointment) {
        Appointment appointment = service.getById(idAppointment);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Appointment>> getAppointmentByUserId(@PathVariable Long userId) {
        List<Appointment> appointment = service.getAppointmentByUserId(userId);
        return ResponseEntity.ok(appointment);
    }

}
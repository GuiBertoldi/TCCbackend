package com.tcc.backend.enums;

import lombok.Getter;

@Getter
public enum AppointmentStatus {
    SCHEDULED(1, "Agendado"),
    COMPLETED(2, "Finalizado"),
    CANCELLED(3, "Cancelado"),
    CONFIRMED(4, "Confirmado");

    private final int code;
    private final String description;

    AppointmentStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
package com.tcc.backend.enums;

import lombok.Getter;

@Getter
public enum AppointmentStatus {
    SCHEDULED(1, "Agendado"),
    COMPLETED(2, "Finalizado"),
    CANCELED(3, "Cancelado");

    private final int code;
    private final String description;

    AppointmentStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
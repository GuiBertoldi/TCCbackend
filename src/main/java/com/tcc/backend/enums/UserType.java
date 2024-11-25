package com.tcc.backend.enums;

import lombok.Getter;

@Getter
public enum UserType {
    ADMIN(1, "Admin"),
    PSICOLOGO(2, "Psicólogo"),
    PACIENTE(3, "Paciente");

    private final int code;
    private final String description;

    UserType(int code, String description) {
        this.code = code;
        this.description = description;
    }
}

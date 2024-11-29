package com.tcc.backend.dtos.session;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SessionRequest {

    @NotNull(message = "O ID do paciente é obrigatório!")
    private Long patientId;

    @NotNull(message = "O ID do psicólogo é obrigatório!")
    private Long idUser;

    @NotNull(message = "A data da sessão é obrigatória!")
    private LocalDate sessionDate;

    @NotBlank(message = "O motivo da sessão é obrigatório!")
    private String reason;

    private String description;
}

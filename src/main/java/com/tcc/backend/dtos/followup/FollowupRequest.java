package com.tcc.backend.dtos.followup;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowupRequest {

    @NotNull(message = "O ID do paciente é obrigatório!")
    private Long patientId;

    @NotBlank(message = "O nome do profissional é obrigatório!")
    private String professionalName;

    @NotBlank(message = "A especialidade do profissional é obrigatória!")
    private String professionalSpecialty;
}

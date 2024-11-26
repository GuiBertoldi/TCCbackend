package com.tcc.backend.dtos.treatment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TreatmentRequest {

    @NotNull(message = "O ID do paciente é obrigatório!")
    private Long patientId;

    @NotBlank(message = "O nome do medicamento é obrigatório!")
    private String medicine;

    @NotNull(message = "A data de início do tratamento é obrigatória!")
    private LocalDate startTreatment;

    private LocalDate endTreatment;
}

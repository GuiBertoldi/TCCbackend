package com.tcc.backend.dtos.documents;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentRequest{

    private Long idDoc;

    @NotBlank(message = "O título deve ser informado!")
    private String titleDoc;

    @NotNull(message = "Um arquivo deve ser anexado!")
    private byte[] typeDoc;
}

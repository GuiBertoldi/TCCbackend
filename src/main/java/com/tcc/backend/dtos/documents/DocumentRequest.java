package com.tcc.backend.dtos.documents;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentRequest{

    @NotBlank(message = "O título deve ser informado!")
    private String titleDoc;

    @NotBlank(message = "Um arquivo deve ser anexado!")
    private byte[] typeDoc;
}

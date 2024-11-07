package com.tcc.backend.dtos.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentResponse {

    private Long idDocument;

    private String titleDoc;

    private String typeDoc;
}

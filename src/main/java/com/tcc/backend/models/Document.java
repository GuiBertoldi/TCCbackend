package com.tcc.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_doc")
    private Long idDoc;
    @Column(name = "title_doc")
    private String titleDoc;
    @Lob
    @Column(name = "type_doc")
    private byte[] typeDoc;
}

package cl.ipla.microcursos.backend.mongo.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "actas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Acta {

    @Id
    private String id;

    private Long inscripcionId;    // id de Inscripcion en SQL
    private Double notaFinal;
    private String observaciones;
    private LocalDateTime fechaEmision;
}

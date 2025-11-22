package cl.ipla.microcursos.backend.mongo.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "rubricas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rubrica {

    @Id
    private String id;

    private String nombre;
    private String descripcion;
    private Long cursoId; // referencia al curso en SQL
}


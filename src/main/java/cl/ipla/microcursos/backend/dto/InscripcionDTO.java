package cl.ipla.microcursos.backend.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InscripcionDTO {

    private Long id;
    private Long cursoId;
    private String tituloCurso;
    private LocalDateTime fechaInscripcion;
    private String estado;   // ACTIVA, FINALIZADA, etc.
    private Integer progreso; // 0–100
}

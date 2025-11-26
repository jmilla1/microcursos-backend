package cl.ipla.microcursos.backend.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InscripcionDTO {

    private Long id;
    private Long cursoId;
    private String tituloCurso;
    private String descripcionCurso;
    private List<ModuloDTO> modulos;
    private LocalDateTime fechaInscripcion;
    private String estado;
    private Integer progreso;
}
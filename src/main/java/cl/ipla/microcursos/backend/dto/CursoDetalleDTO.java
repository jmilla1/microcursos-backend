package cl.ipla.microcursos.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CursoDetalleDTO {

    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private String nivel;
    private Integer duracionMinutos;

    // Información adicional opcional: ajusta a tu entidad Curso
    private String objetivos;       // o List<String> si lo tienes así
    private String requisitos;

    // Relación con módulos
    private List<ModuloDTO> modulos;
}

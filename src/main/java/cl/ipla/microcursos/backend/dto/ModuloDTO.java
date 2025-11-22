package cl.ipla.microcursos.backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModuloDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private Integer orden;
}
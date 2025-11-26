package cl.ipla.microcursos.backend.mapper;

import cl.ipla.microcursos.backend.dto.InscripcionDTO;
import cl.ipla.microcursos.backend.dto.ModuloDTO;
import cl.ipla.microcursos.backend.model.Inscripcion;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.List;

public class InscripcionMapper {

    private InscripcionMapper() {
    }

    public static InscripcionDTO toInscripcionDTO(Inscripcion inscripcion) {
        if (inscripcion == null) {
            return null;
        }

        List<ModuloDTO> modulosDTO = (inscripcion.getCurso().getModulos() == null)
                ? Collections.emptyList()
                : inscripcion.getCurso().getModulos().stream()
                        .map(CursoMapper::toModuloDTO)
                        .collect(Collectors.toList());

        return InscripcionDTO.builder()
                .id(inscripcion.getId())
                .cursoId(inscripcion.getCurso().getId())
                .tituloCurso(inscripcion.getCurso().getTitulo())
                .descripcionCurso(inscripcion.getCurso().getDescripcion())
                .modulos(modulosDTO)
                .fechaInscripcion(inscripcion.getFechaInscripcion())
                .estado(inscripcion.getEstado())
                .progreso(inscripcion.getProgreso())
                .build();
    }
}
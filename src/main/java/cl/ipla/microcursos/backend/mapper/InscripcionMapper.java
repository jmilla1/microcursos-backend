package cl.ipla.microcursos.backend.mapper;

import cl.ipla.microcursos.backend.dto.InscripcionDTO;
import cl.ipla.microcursos.backend.model.Inscripcion;

public class InscripcionMapper {

    private InscripcionMapper() {
    }

    public static InscripcionDTO toInscripcionDTO(Inscripcion inscripcion) {
        if (inscripcion == null) {
            return null;
        }

        return InscripcionDTO.builder()
                .id(inscripcion.getId())
                .cursoId(inscripcion.getCurso().getId())
                .tituloCurso(inscripcion.getCurso().getTitulo())
                .fechaInscripcion(inscripcion.getFechaInscripcion())
                .estado(inscripcion.getEstado())
                .progreso(inscripcion.getProgreso())
                .build();
    }
}

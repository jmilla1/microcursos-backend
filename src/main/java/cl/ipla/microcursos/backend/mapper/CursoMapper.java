package cl.ipla.microcursos.backend.mapper;

import cl.ipla.microcursos.backend.dto.CursoDTO;
import cl.ipla.microcursos.backend.dto.ModuloDTO;
import cl.ipla.microcursos.backend.model.Curso;
import cl.ipla.microcursos.backend.model.Modulo;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CursoMapper {

    private CursoMapper() {
    }

    public static CursoDTO toCursoDTO(Curso curso) {
        if (curso == null) {
            return null;
        }

        List<ModuloDTO> moduloDTOs = (curso.getModulos() == null)
                ? Collections.emptyList()
                : curso.getModulos().stream()
                    .map(CursoMapper::toModuloDTO)
                    .collect(Collectors.toList());

        return CursoDTO.builder()
                .id(curso.getId())
                .titulo(curso.getTitulo())
                .descripcion(curso.getDescripcion())
                .fechaCreacion(curso.getFechaCreacion())
                .modulos(moduloDTOs)
                .build();
    }

    public static ModuloDTO toModuloDTO(Modulo modulo) {
        if (modulo == null) {
            return null;
        }

        return ModuloDTO.builder()
                .id(modulo.getId())
                .titulo(modulo.getTitulo())
                .descripcion(modulo.getDescripcion())
                .orden(modulo.getOrden())
                .build();
    }
}


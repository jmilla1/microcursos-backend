package cl.ipla.microcursos.backend.service;

import cl.ipla.microcursos.backend.model.Curso;

import java.util.List;

public interface CursoService {

    List<Curso> obtenerTodos();

    Curso crear(Curso curso);
    
    Curso obtenerPorId(Long id);

    void eliminar(Long id);
}

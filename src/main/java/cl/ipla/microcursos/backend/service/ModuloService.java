package cl.ipla.microcursos.backend.service;

import cl.ipla.microcursos.backend.model.Modulo;

import java.util.List;

public interface ModuloService {

    Modulo obtenerPorId(Long id);

    List<Modulo> obtenerPorCurso(Long cursoId);

    Modulo crearModulo(Long cursoId, Modulo modulo);

    Modulo actualizarModulo(Long id, Modulo modulo);

    void eliminarModulo(Long id);
}

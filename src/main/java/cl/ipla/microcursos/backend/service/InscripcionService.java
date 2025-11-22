package cl.ipla.microcursos.backend.service;

import cl.ipla.microcursos.backend.model.Inscripcion;

import java.util.List;

public interface InscripcionService {

    Inscripcion inscribirUsuarioEnCurso(String emailUsuario, Long cursoId);

    List<Inscripcion> obtenerInscripcionesDeUsuario(String emailUsuario);

    Inscripcion actualizarProgreso(Long inscripcionId, int nuevoProgreso);

    Inscripcion marcarModuloComoCompletado(String emailUsuario, Long cursoId, Long moduloId);
}

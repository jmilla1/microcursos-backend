package cl.ipla.microcursos.backend.service;

import cl.ipla.microcursos.backend.exception.RecursoNoEncontradoException;
import cl.ipla.microcursos.backend.model.Curso;
import cl.ipla.microcursos.backend.model.Modulo;
import cl.ipla.microcursos.backend.repository.CursoRepository;
import cl.ipla.microcursos.backend.repository.ModuloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModuloServiceImpl implements ModuloService {

    private final ModuloRepository moduloRepository;
    private final CursoRepository cursoRepository;

    @Override
    @Transactional(readOnly = true)
    public Modulo obtenerPorId(Long id) {
        return moduloRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Módulo con id " + id + " no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Modulo> obtenerPorCurso(Long cursoId) {
        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Curso con id " + cursoId + " no encontrado"));

        // Forzamos la carga de la colección dentro de la transacción
        List<Modulo> modulos = curso.getModulos();
        modulos.size();
        return modulos;
    }

    @Override
    @Transactional
    public Modulo crearModulo(Long cursoId, Modulo modulo) {
        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Curso con id " + cursoId + " no encontrado"));

        modulo.setId(null);         // nos aseguramos que sea nuevo
        modulo.setCurso(curso);     // vinculamos al curso

        return moduloRepository.save(modulo);
    }

    @Override
    @Transactional
    public Modulo actualizarModulo(Long id, Modulo datos) {
        Modulo existente = obtenerPorId(id);

        existente.setTitulo(datos.getTitulo());
        existente.setDescripcion(datos.getDescripcion());
        existente.setOrden(datos.getOrden());

        return moduloRepository.save(existente);
    }

    @Override
    @Transactional
    public void eliminarModulo(Long id) {
        Modulo existente = obtenerPorId(id);
        moduloRepository.delete(existente);
    }
}

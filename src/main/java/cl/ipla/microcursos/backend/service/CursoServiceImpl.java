package cl.ipla.microcursos.backend.service;

import cl.ipla.microcursos.backend.dto.CursoRequestDTO;
import cl.ipla.microcursos.backend.exception.RecursoNoEncontradoException;
import cl.ipla.microcursos.backend.model.Curso;
import cl.ipla.microcursos.backend.model.Modulo;
import cl.ipla.microcursos.backend.repository.CursoRepository;
import cl.ipla.microcursos.backend.repository.InscripcionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CursoServiceImpl implements CursoService {

    private final CursoRepository cursoRepository;
    private final InscripcionRepository inscripcionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Curso> obtenerTodos() {
        return cursoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Curso obtenerPorId(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Curso con id " + id + " no encontrado"));
    }

    @Override
    @Transactional
    public Curso crear(Curso curso) {
        // Asegurarse de que es una nueva entidad
        // curso.setId(null);
        if (curso.getId() == null && curso.getFechaCreacion() == null) {
            curso.setFechaCreacion(LocalDateTime.now());
        }

        if (curso.getFechaCreacion() == null) {
            curso.setFechaCreacion(LocalDateTime.now());
        }

        if (curso.getModulos() != null) {
            for (Modulo modulo : curso.getModulos()) {
                modulo.setId(null);
                modulo.setCurso(curso); // establecer la relación inversa
            }
        }

        return cursoRepository.save(curso);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Curso con id " + id + " no encontrado"));

        if (inscripcionRepository.existsByCurso(curso)) {
            throw new IllegalStateException(
                    "No se puede eliminar un curso que tiene inscripciones asociadas.");
        }

        cursoRepository.delete(curso);
    }

    @Override
    @Transactional
    public Curso actualizar(Long id, CursoRequestDTO cursoDatos) {
        // 1. Buscamos el curso existente
        Curso cursoExistente = cursoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Curso con id " + id + " no encontrado"));

        // 2. Actualizamos SOLO los datos permitidos
        cursoExistente.setTitulo(cursoDatos.getTitulo());
        cursoExistente.setDescripcion(cursoDatos.getDescripcion());

        // 3. Guardamos (Hibernate detecta que ya tiene ID y hace un UPDATE, no un
        // INSERT)
        return cursoRepository.save(cursoExistente);
    }
}

package cl.ipla.microcursos.backend.service;

import cl.ipla.microcursos.backend.exception.RecursoNoEncontradoException;
import cl.ipla.microcursos.backend.model.*;
import cl.ipla.microcursos.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InscripcionServiceImpl implements InscripcionService {

    private final InscripcionRepository inscripcionRepository;
    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;
    private final ModuloRepository moduloRepository;
    private final ModuloProgresoRepository moduloProgresoRepository;

    @Override
    @Transactional
    public Inscripcion inscribirUsuarioEnCurso(String emailUsuario, Long cursoId) {

        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Usuario con email " + emailUsuario + " no encontrado"));

        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Curso con id " + cursoId + " no encontrado"));

        return inscripcionRepository.findByUsuarioAndCurso(usuario, curso)
                .orElseGet(() -> {
                    Inscripcion nueva = Inscripcion.builder()
                            .usuario(usuario)
                            .curso(curso)
                            .fechaInscripcion(LocalDateTime.now())
                            .estado("ACTIVA")
                            .progreso(0)
                            .build();
                    return inscripcionRepository.save(nueva);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inscripcion> obtenerInscripcionesDeUsuario(String emailUsuario) {

        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Usuario con email " + emailUsuario + " no encontrado"));

        return inscripcionRepository.findByUsuario(usuario);
    }

    @Override
    @Transactional
    public Inscripcion actualizarProgreso(Long inscripcionId, int nuevoProgreso) {

        if (nuevoProgreso < 0 || nuevoProgreso > 100) {
            throw new IllegalArgumentException("El progreso debe estar entre 0 y 100.");
        }

        Inscripcion inscripcion = inscripcionRepository.findById(inscripcionId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Inscripción con id " + inscripcionId + " no encontrada"));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String emailActual = auth.getName();

        if (inscripcion.getUsuario() == null ||
                inscripcion.getUsuario().getEmail() == null ||
                !inscripcion.getUsuario().getEmail().equals(emailActual)) {
            throw new IllegalArgumentException("No puedes modificar inscripciones de otro usuario.");
        }

        inscripcion.setProgreso(nuevoProgreso);

        if (nuevoProgreso >= 100) {
            inscripcion.setEstado("FINALIZADA");
        } else if (nuevoProgreso > 0 && !"ACTIVA".equals(inscripcion.getEstado())) {
            inscripcion.setEstado("ACTIVA");
        }

        return inscripcionRepository.save(inscripcion);
    }

    // 👉 NUEVO: marcar un módulo como completado y recalcular progreso
    @Override
    @Transactional
    public Inscripcion marcarModuloComoCompletado(String emailUsuario, Long cursoId, Long moduloId) {

        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Usuario con email " + emailUsuario + " no encontrado"));

        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Curso con id " + cursoId + " no encontrado"));

        Inscripcion inscripcion = inscripcionRepository.findByUsuarioAndCurso(usuario, curso)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("El usuario no está inscrito en este curso"));

        Modulo modulo = moduloRepository.findById(moduloId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Módulo con id " + moduloId + " no encontrado"));

        if (!curso.getModulos().contains(modulo)) {
            throw new IllegalArgumentException("El módulo no pertenece a este curso.");
        }

        // Registrar el módulo como completado solo si no existe ya
        if (!moduloProgresoRepository.existsByInscripcionAndModulo(inscripcion, modulo)) {
            ModuloProgreso progresoModulo = ModuloProgreso.builder()
                    .inscripcion(inscripcion)
                    .modulo(modulo)
                    .fechaCompletado(LocalDateTime.now())
                    .build();
            moduloProgresoRepository.save(progresoModulo);
        }

        // Recalcular progreso según módulos completados
        int totalModulos = curso.getModulos().size();
        if (totalModulos == 0) {
            inscripcion.setProgreso(0);
            inscripcion.setEstado("ACTIVA");
        } else {
            long completados = moduloProgresoRepository.countByInscripcion(inscripcion);
            int porcentaje = (int) Math.round((completados * 100.0) / totalModulos);

            inscripcion.setProgreso(porcentaje);

            if (porcentaje >= 100) {
                inscripcion.setEstado("FINALIZADA");
            } else if (porcentaje > 0) {
                inscripcion.setEstado("ACTIVA");
            }
        }

        return inscripcionRepository.save(inscripcion);
    }
}

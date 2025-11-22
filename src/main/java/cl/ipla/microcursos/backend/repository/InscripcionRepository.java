package cl.ipla.microcursos.backend.repository;

import cl.ipla.microcursos.backend.model.Curso;
import cl.ipla.microcursos.backend.model.Inscripcion;
import cl.ipla.microcursos.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

    boolean existsByUsuarioAndCurso(Usuario usuario, Curso curso);

    Optional<Inscripcion> findByUsuarioAndCurso(Usuario usuario, Curso curso);

    List<Inscripcion> findByUsuario(Usuario usuario);

    boolean existsByCurso(Curso curso);
}

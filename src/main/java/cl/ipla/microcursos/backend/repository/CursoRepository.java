package cl.ipla.microcursos.backend.repository;

import cl.ipla.microcursos.backend.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {
}


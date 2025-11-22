package cl.ipla.microcursos.backend.repository;

import cl.ipla.microcursos.backend.model.Inscripcion;
import cl.ipla.microcursos.backend.model.Modulo;
import cl.ipla.microcursos.backend.model.ModuloProgreso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuloProgresoRepository extends JpaRepository<ModuloProgreso, Long> {

    boolean existsByInscripcionAndModulo(Inscripcion inscripcion, Modulo modulo);

    long countByInscripcion(Inscripcion inscripcion);
}

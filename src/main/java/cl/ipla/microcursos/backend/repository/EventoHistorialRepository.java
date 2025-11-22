package cl.ipla.microcursos.backend.repository;

import cl.ipla.microcursos.backend.model.EventoHistorial;
import cl.ipla.microcursos.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventoHistorialRepository extends JpaRepository<EventoHistorial, Long> {

    List<EventoHistorial> findByUsuarioOrderByFechaDesc(Usuario usuario);
}

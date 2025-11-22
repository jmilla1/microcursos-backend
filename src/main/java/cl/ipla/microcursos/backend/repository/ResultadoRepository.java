package cl.ipla.microcursos.backend.repository;

import cl.ipla.microcursos.backend.model.Resultado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResultadoRepository extends JpaRepository<Resultado, Long> {
}


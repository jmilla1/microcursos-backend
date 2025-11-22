package cl.ipla.microcursos.backend.repository;

import cl.ipla.microcursos.backend.model.HealthCheck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthCheckRepository extends JpaRepository<HealthCheck, Long> {
}


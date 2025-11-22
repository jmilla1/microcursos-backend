package cl.ipla.microcursos.backend.mongo.repository;

import cl.ipla.microcursos.backend.mongo.model.Rubrica;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RubricaRepository extends MongoRepository<Rubrica, String> {
}


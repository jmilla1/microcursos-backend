package cl.ipla.microcursos.backend.mongo.repository;

import cl.ipla.microcursos.backend.mongo.model.Acta;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ActaRepository extends MongoRepository<Acta, String> {
}

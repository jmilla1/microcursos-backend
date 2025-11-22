package cl.ipla.microcursos.backend.service;

public interface CertificadoService {

    byte[] generarCertificadoPdf(String emailUsuario, Long cursoId);
}

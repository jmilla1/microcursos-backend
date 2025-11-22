package cl.ipla.microcursos.backend.service;

public interface HistorialService {

    void registrarEvento(String emailUsuario, String tipo, String detalle);
}

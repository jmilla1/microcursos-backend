package cl.ipla.microcursos.backend.service;

import cl.ipla.microcursos.backend.model.EventoHistorial;
import cl.ipla.microcursos.backend.model.Usuario;
import cl.ipla.microcursos.backend.repository.EventoHistorialRepository;
import cl.ipla.microcursos.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class HistorialServiceImpl implements HistorialService {

    private final UsuarioRepository usuarioRepository;
    private final EventoHistorialRepository historialRepository;

    @Override
    public void registrarEvento(String emailUsuario, String tipo, String detalle) {

        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Usuario con email " + emailUsuario + " no encontrado"));

        EventoHistorial evento = EventoHistorial.builder()
                .usuario(usuario)
                .tipo(tipo)
                .detalle(detalle)
                .fecha(LocalDateTime.now())
                .build();

        historialRepository.save(evento);
    }
}

package cl.ipla.microcursos.backend.controller;

import cl.ipla.microcursos.backend.model.Rol;
import cl.ipla.microcursos.backend.model.Usuario;
import cl.ipla.microcursos.backend.repository.RolRepository;
import cl.ipla.microcursos.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminUserController {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository; // Necesario para buscar roles
    private final PasswordEncoder passwordEncoder; // Necesario para encriptar pass

    // 1. Listar todos los usuarios
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Usuario>> listarTodos() {
        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    // 2. Crear usuario (ADMIN)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("El email ya existe");
        }

        // Encriptar contraseña
        usuario.setPasswordHash(passwordEncoder.encode(usuario.getPasswordHash()));

        // Asignar Rol (Si viene nulo, por defecto ESTUDIANTE)
        if (usuario.getRol() != null && usuario.getRol().getId() != null) {
            Rol rolDb = rolRepository.findById(usuario.getRol().getId())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
            usuario.setRol(rolDb);
        } else {
            // Asignar rol por defecto si es necesario, o lanzar error
            Rol rolEstudiante = rolRepository.findById(2L).orElse(null); // Asumiendo ID 2 es estudiante
            usuario.setRol(rolEstudiante);
        }

        Usuario guardado = usuarioRepository.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    // 3. Actualizar usuario (ADMIN)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioDatos) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuarioExistente.setNombre(usuarioDatos.getNombre());
        usuarioExistente.setEmail(usuarioDatos.getEmail());

        // Actualizar contraseña solo si viene informada (no vacía)
        if (usuarioDatos.getPasswordHash() != null && !usuarioDatos.getPasswordHash().isEmpty()) {
            usuarioExistente.setPasswordHash(passwordEncoder.encode(usuarioDatos.getPasswordHash()));
        }

        // Actualizar Rol
        if (usuarioDatos.getRol() != null && usuarioDatos.getRol().getId() != null) {
            Rol rolDb = rolRepository.findById(usuarioDatos.getRol().getId())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
            usuarioExistente.setRol(rolDb);
        }

        Usuario actualizado = usuarioRepository.save(usuarioExistente);
        return ResponseEntity.ok(actualizado);
    }

    // 4. Eliminar usuario
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        usuarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
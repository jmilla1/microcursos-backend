package cl.ipla.microcursos.backend.auth;

import cl.ipla.microcursos.backend.model.Rol;
import cl.ipla.microcursos.backend.model.Usuario;
import cl.ipla.microcursos.backend.repository.RolRepository;
import cl.ipla.microcursos.backend.repository.UsuarioRepository;
import cl.ipla.microcursos.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Rol estudianteRole = rolRepository.findByNombre("ESTUDIANTE")
                .orElseThrow(() -> new IllegalStateException("Rol ESTUDIANTE no existe"));

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .rol(estudianteRole)
                .build();

        usuarioRepository.save(usuario);

        User springUser = (User) User.builder()
                .username(usuario.getEmail())
                .password(usuario.getPasswordHash())
                .authorities("ROLE_" + estudianteRole.getNombre())
                .build();

        String token = jwtService.generateToken(springUser);

        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User principal = (User) auth.getPrincipal();
        String token = jwtService.generateToken(principal);

        return ResponseEntity.ok(new AuthResponse(token));
    }
}

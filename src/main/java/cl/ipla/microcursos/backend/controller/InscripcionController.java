package cl.ipla.microcursos.backend.controller;

import cl.ipla.microcursos.backend.dto.ActualizarProgresoRequest;
import cl.ipla.microcursos.backend.dto.InscripcionDTO;
import cl.ipla.microcursos.backend.mapper.InscripcionMapper;
import cl.ipla.microcursos.backend.model.Inscripcion;
import cl.ipla.microcursos.backend.service.InscripcionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class InscripcionController {

    private final InscripcionService inscripcionService;

    // Inscribirse en un curso
    @PreAuthorize("hasAnyRole('ADMIN','ESTUDIANTE')")
    @PostMapping("/cursos/{cursoId}/inscripciones")
    public ResponseEntity<InscripcionDTO> inscribirEnCurso(
            @PathVariable Long cursoId,
            Authentication authentication
    ) {
        String emailUsuario = authentication.getName();

        Inscripcion inscripcion =
                inscripcionService.inscribirUsuarioEnCurso(emailUsuario, cursoId);

        InscripcionDTO dto = InscripcionMapper.toInscripcionDTO(inscripcion);

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    // Listar "mis cursos" (inscripciones del usuario autenticado)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mis-cursos")
    public ResponseEntity<List<InscripcionDTO>> obtenerMisCursos(
            Authentication authentication
    ) {
        String emailUsuario = authentication.getName();

        List<Inscripcion> inscripciones =
                inscripcionService.obtenerInscripcionesDeUsuario(emailUsuario);

        List<InscripcionDTO> dtos = inscripciones.stream()
                .map(InscripcionMapper::toInscripcionDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

   // Actualizar progreso de una inscripción concreta
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/mis-cursos/{inscripcionId}/progreso")
    public ResponseEntity<InscripcionDTO> actualizarProgreso(
            @PathVariable Long inscripcionId,
            @Valid @RequestBody ActualizarProgresoRequest request
    ) {
        Inscripcion actualizada =
                inscripcionService.actualizarProgreso(inscripcionId, request.getProgreso());

        return ResponseEntity.ok(InscripcionMapper.toInscripcionDTO(actualizada));
    }
    
    // Marcar un módulo como completado
    @PreAuthorize("hasAnyRole('ADMIN','ESTUDIANTE')")
    @PostMapping("/cursos/{cursoId}/modulos/{moduloId}/completado")
    public ResponseEntity<InscripcionDTO> marcarModuloCompletado(
        @PathVariable Long cursoId,
        @PathVariable Long moduloId,
        Authentication authentication
    ) {
        String emailUsuario = authentication.getName();
        Inscripcion inscripcion = inscripcionService.marcarModuloComoCompletado(
            emailUsuario,
            cursoId,
            moduloId
        );
        return ResponseEntity.ok(InscripcionMapper.toInscripcionDTO(inscripcion));
      }
}


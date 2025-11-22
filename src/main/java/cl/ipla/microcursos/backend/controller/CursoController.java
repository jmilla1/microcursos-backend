package cl.ipla.microcursos.backend.controller;

import cl.ipla.microcursos.backend.dto.CursoDTO;
import cl.ipla.microcursos.backend.dto.ModuloDTO;
import cl.ipla.microcursos.backend.mapper.CursoMapper;
import cl.ipla.microcursos.backend.model.Curso;
import cl.ipla.microcursos.backend.model.Modulo;
import cl.ipla.microcursos.backend.service.CursoService;
import cl.ipla.microcursos.backend.service.ModuloService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cursos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CursoController {

    private final CursoService cursoService;
    private final ModuloService moduloService;

    // Listado de cursos
    @GetMapping
    public ResponseEntity<List<CursoDTO>> obtenerTodos() {
        List<Curso> cursos = cursoService.obtenerTodos();
        List<CursoDTO> dtos = cursos.stream()
                .map(CursoMapper::toCursoDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Detalle de curso por id
    @GetMapping("/{id}")
    public ResponseEntity<CursoDTO> obtenerPorId(@PathVariable Long id) {
        Curso curso = cursoService.obtenerPorId(id);
        CursoDTO dto = CursoMapper.toCursoDTO(curso);
        return ResponseEntity.ok(dto);
    }

    // Crear nuevo curso (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CursoDTO> crear(
            @RequestBody Curso curso,
            org.springframework.security.core.Authentication authentication
    ) {
        // 🔍 DEBUG: ver qué ve Spring Security
        System.out.println("== CREAR CURSO ==");
        System.out.println("Usuario autenticado: " + authentication.getName());
        System.out.println("Authorities: " + authentication.getAuthorities());

        Curso creado = cursoService.crear(curso);
        CursoDTO dto = CursoMapper.toCursoDTO(creado);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }



    // ======== MÓDULOS POR CURSO ========

    // Listar módulos de un curso específico
    @GetMapping("/{cursoId}/modulos")
    public ResponseEntity<List<ModuloDTO>> obtenerModulosPorCurso(@PathVariable Long cursoId) {
        List<Modulo> modulos = moduloService.obtenerPorCurso(cursoId);
        List<ModuloDTO> dtos = modulos.stream()
                .map(CursoMapper::toModuloDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Crear un módulo dentro de un curso (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{cursoId}/modulos")
    public ResponseEntity<ModuloDTO> crearModulo(
            @PathVariable Long cursoId,
            @RequestBody Modulo modulo) {
        Modulo creado = moduloService.crearModulo(cursoId, modulo);
        ModuloDTO dto = CursoMapper.toModuloDTO(creado);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
    // Eliminar un curso (solo ADMIN)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        cursoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}

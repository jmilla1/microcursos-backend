package cl.ipla.microcursos.backend.controller;

import cl.ipla.microcursos.backend.dto.ModuloDTO;
import cl.ipla.microcursos.backend.mapper.CursoMapper;
import cl.ipla.microcursos.backend.model.Modulo;
import cl.ipla.microcursos.backend.service.ModuloService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/modulos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ModuloController {

    private final ModuloService moduloService;

    // Detalle de módulo por id
    @GetMapping("/{id}")
    public ResponseEntity<ModuloDTO> obtenerPorId(@PathVariable Long id) {
        Modulo modulo = moduloService.obtenerPorId(id);
        ModuloDTO dto = CursoMapper.toModuloDTO(modulo);
        return ResponseEntity.ok(dto);
    }

    // Actualizar módulo (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ModuloDTO> actualizar(
            @PathVariable Long id,
            @RequestBody Modulo moduloDatos
    ) {
        Modulo actualizado = moduloService.actualizarModulo(id, moduloDatos);
        ModuloDTO dto = CursoMapper.toModuloDTO(actualizado);
        return ResponseEntity.ok(dto);
    }

    // Eliminar módulo (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        moduloService.eliminarModulo(id);
        return ResponseEntity.noContent().build();
    }
}

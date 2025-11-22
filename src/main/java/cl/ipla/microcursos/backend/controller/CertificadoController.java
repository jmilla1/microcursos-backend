package cl.ipla.microcursos.backend.controller;

import cl.ipla.microcursos.backend.service.CertificadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cursos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CertificadoController {

    private final CertificadoService certificadoService;

    @PreAuthorize("hasAnyRole('ADMIN','ESTUDIANTE')")
    @GetMapping("/{cursoId}/certificado")
    public ResponseEntity<byte[]> descargarCertificado(
            @PathVariable Long cursoId,
            Authentication authentication
    ) {
        String emailUsuario = authentication.getName();
        byte[] pdf = certificadoService.generarCertificadoPdf(emailUsuario, cursoId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData(
                "attachment",
                "certificado_curso_" + cursoId + ".pdf"
        );

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
}

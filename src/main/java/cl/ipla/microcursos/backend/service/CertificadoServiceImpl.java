package cl.ipla.microcursos.backend.service;

import cl.ipla.microcursos.backend.exception.RecursoNoEncontradoException;
import cl.ipla.microcursos.backend.model.Curso;
import cl.ipla.microcursos.backend.model.Inscripcion;
import cl.ipla.microcursos.backend.repository.CursoRepository;
import cl.ipla.microcursos.backend.repository.InscripcionRepository;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class CertificadoServiceImpl implements CertificadoService {

    private final CursoRepository cursoRepository;
    private final InscripcionRepository inscripcionRepository;
    private final HistorialService historialService;

    @Override
    public byte[] generarCertificadoPdf(String emailUsuario, Long cursoId) {

        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Curso con id " + cursoId + " no encontrado"));

        Inscripcion inscripcion = inscripcionRepository.findAll().stream()
                .filter(i -> i.getCurso().getId().equals(cursoId)
                        && i.getUsuario() != null
                        && emailUsuario.equals(i.getUsuario().getEmail()))
                .findFirst()
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró inscripción del usuario en este curso"));

        if (inscripcion.getProgreso() == null || inscripcion.getProgreso() < 100) {
            throw new IllegalStateException("El curso aún no está completado por el estudiante.");
        }

        byte[] pdfBytes = construirPdf(inscripcion, curso);

        // Registrar evento en historial (RF-12)
        historialService.registrarEvento(
                emailUsuario,
                "CERTIFICADO_EMITIDO",
                "cursoId=" + cursoId
        );

        return pdfBytes;
    }

    private byte[] construirPdf(Inscripcion inscripcion, Curso curso) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Document document = new Document();
        PdfWriter.getInstance(document, baos);
        document.open();

        Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
        Font textoFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

        document.add(new Paragraph("Certificado de Finalización", tituloFont));
        document.add(new Paragraph("\n\n"));

        document.add(new Paragraph(
                "Se certifica que " + inscripcion.getUsuario().getNombre()
                        + " ha completado satisfactoriamente el curso:",
                textoFont));

        document.add(new Paragraph("\n\"" + curso.getTitulo() + "\"\n", tituloFont));
        document.add(new Paragraph(
                "Con un progreso del 100% de los módulos definidos para el curso.",
                textoFont));

        document.add(new Paragraph("\nFecha de emisión: " +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))));

        document.close();
        return baos.toByteArray();
    }
}

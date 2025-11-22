package cl.ipla.microcursos.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "eventos_historial")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventoHistorial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Usuario que realiza la acción (admin o estudiante)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Ejemplos: CERTIFICADO_EMITIDO, REPORTE_EXPORTADO
    @Column(nullable = false, length = 50)
    private String tipo;

    // Texto corto con datos del contexto (cursoId, rango fechas, etc.)
    @Column(length = 500)
    private String detalle;

    @Column(nullable = false)
    private LocalDateTime fecha;
}

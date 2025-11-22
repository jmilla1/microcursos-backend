package cl.ipla.microcursos.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "resultados")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resultado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inscripcion_id", nullable = false)
    private Inscripcion inscripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modulo_id")
    private Modulo modulo; // opcional

    @Column(nullable = false)
    private Double puntaje;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;
}

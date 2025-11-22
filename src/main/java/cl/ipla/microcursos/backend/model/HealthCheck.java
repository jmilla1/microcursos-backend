package cl.ipla.microcursos.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "HealthCheck")
public class HealthCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mensaje;

    public Long getId() {
        return id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}


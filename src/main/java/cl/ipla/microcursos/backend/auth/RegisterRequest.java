package cl.ipla.microcursos.backend.auth;

import lombok.Data;

@Data
public class RegisterRequest {
    private String nombre;
    private String email;
    private String password;
}


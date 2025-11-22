package cl.ipla.microcursos.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorDTO {

    private int statusCode;
    private String error;
    private String message;
    private String path;
    private LocalDateTime timestamp;
}

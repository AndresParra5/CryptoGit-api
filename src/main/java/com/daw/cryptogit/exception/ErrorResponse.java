package com.daw.cryptogit.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private int status;
    private String mensaje;
    private LocalDateTime timestamp;
    private Map<String, String> erroresCampo; // Mapea los errores DTO campo -> mensaje[cite: 1]

    // Constructor alternativo para errores generales sin mapa de campos[cite: 3]
    public ErrorResponse(int status, String mensaje, LocalDateTime timestamp) {
        this.status = status;
        this.mensaje = mensaje;
        this.timestamp = timestamp;
        this.erroresCampo = null;
    }
}
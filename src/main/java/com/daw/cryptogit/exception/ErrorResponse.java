package com.daw.cryptogit.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Estructura estándar de respuesta para todos los errores HTTP de la API")
public class ErrorResponse {

    @Schema(description = "Código de estado HTTP del error", example = "400")
    private int status;

    @Schema(description = "Descripción legible del error o causa del fallo", example = "Error de validación en los datos de entrada")
    private String mensaje;

    @Schema(description = "Fecha y hora exacta en la que se produjo la excepción", example = "2026-11-01T21:18:00")
    private LocalDateTime timestamp;

    @Schema(description = "Mapa detallado con los errores por campo en validaciones DTO (campo -> mensaje de error). Nulo si no aplica.",
            example = "{\"simbolo\": \"El símbolo es obligatorio\", \"cantidad\": \"La cantidad debe ser un número positivo\"}")
    private Map<String, String> erroresCampo; // Mapea los errores DTO campo -> mensaje

    // Constructor alternativo para errores generales sin mapa de campos
    public ErrorResponse(int status, String mensaje, LocalDateTime timestamp) {
        this.status = status;
        this.mensaje = mensaje;
        this.timestamp = timestamp;
        this.erroresCampo = null;
    }
}
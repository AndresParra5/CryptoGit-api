package com.daw.cryptogit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de entrada para la actualización parcial (PATCH) de un activo cripto. Todos los campos son opcionales.")
public class PatchActivoDTO {

    @Schema(description = "Nuevo nombre completo del activo (opcional)", example = "Bitcoin Cash")
    private String nombre;

    @PositiveOrZero(message = "La cantidad no puede ser negativa")
    @Schema(description = "Nueva cantidad a registrar (opcional, mayor o igual a 0)", example = "1.25")
    private Double cantidad;

    @Positive(message = "El precio de compra debe ser mayor que 0")
    @Schema(description = "Nuevo precio de compra en EUR (opcional, positivo)", example = "58000.00")
    private Double precioCompraEur;

    @Email(message = "Debe proporcionar un formato de email válido")
    @Schema(description = "Nuevo email del analista asignado (opcional)", example = "nuevo.analista@crypto.com")
    private String emailAnalista;

    @PastOrPresent(message = "La fecha de adquisición no puede ser futura")
    @Schema(description = "Nueva fecha de adquisición (opcional, pasada o presente)", example = "2026-02-01")
    private LocalDate fechaAdquisicion;

    @Future(message = "La fecha de revisión debe ser una fecha futura")
    @Schema(description = "Nueva fecha prevista de revisión (opcional, estrictamente futura)", example = "2027-06-30")
    private LocalDate fechaRevision;

    @Schema(description = "Nuevo estado de aceptación de riesgo (opcional)", example = "true")
    private Boolean riesgoAceptado;

    @Schema(description = "Nueva lista completa de etiquetas para reemplazar las existentes (opcional)", example = "[\"L1\", \"DEFI\"]")
    private List<String> etiquetas;
}
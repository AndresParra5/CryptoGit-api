package com.daw.cryptogit.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatchActivoDTO {

    private String nombre;

    @PositiveOrZero(message = "La cantidad no puede ser negativa")
    private Double cantidad;

    @Positive(message = "El precio de compra debe ser mayor que 0")
    private Double precioCompraEur;

    @Email(message = "Debe proporcionar un formato de email válido")
    private String emailAnalista;

    @PastOrPresent(message = "La fecha de adquisición no puede ser futura")
    private LocalDate fechaAdquisicion;

    @Future(message = "La fecha de revisión debe ser una fecha futura")
    private LocalDate fechaRevision;

    private Boolean riesgoAceptado;

    private List<String> etiquetas;
}
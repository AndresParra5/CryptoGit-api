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
@Schema(description = "DTO de entrada para la creación o actualización completa (PUT) de un activo cripto")
public class CrearActivoDTO {

    // 1. Regex: De 2 a 10 letras (ej. btc, eth)
    @NotBlank(message = "El símbolo es obligatorio")
    @Pattern(regexp = "^[a-zA-Z]{2,10}$", message = "El símbolo debe contener entre 2 y 10 letras")
    @Schema(description = "Símbolo/Ticker identificador en CoinGecko (entre 2 y 10 letras)", example = "bitcoin")
    private String simbolo;

    // 2. Tamaño entre 3 y 50 caracteres
    @NotBlank(message = "El nombre del activo es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    @Schema(description = "Nombre completo del activo (3 a 50 caracteres)", example = "Bitcoin")
    private String nombre;

    // 3. Control de precisión decimal
    @NotNull(message = "La cantidad no puede ser nula")
    @Positive(message = "La cantidad debe ser un número positivo mayor que cero")
    @Digits(integer = 8, fraction = 8, message = "La cantidad no puede superar los 8 enteros y 8 decimales")
    @Schema(description = "Cantidad adquirida (positivo, máx 8 decimales)", example = "0.5")
    private Double cantidad;

    // 4. Suelo mínimo en el precio
    @NotNull(message = "El precio de compra es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio de compra debe ser de al menos 0.01 EUR")
    @Schema(description = "Precio unitario de compra original en EUR (mínimo 0.01)", example = "55000.00")
    private Double precioCompraEur;

    // 5. Formato de correo electrónico válido
    @NotBlank(message = "El email del analista es obligatorio")
    @Email(message = "Debe proporcionar una dirección de email válida (ej. analista@crypto.com)")
    @Schema(description = "Correo electrónico del analista asignado", example = "analista@crypto.com")
    private String emailAnalista;

    // 6. Fecha pasada o presente
    @NotNull(message = "La fecha de adquisición es obligatoria")
    @PastOrPresent(message = "La fecha de adquisición no puede estar en el futuro")
    @Schema(description = "Fecha de compra del activo (pasada o presente)", example = "2026-01-15")
    private LocalDate fechaAdquisicion;

    // 7. Fecha estrictamente futura
    @NotNull(message = "La fecha de revisión no puede ser nula")
    @Future(message = "La fecha de revisión debe ser una fecha FUTURA")
    @Schema(description = "Fecha programada para revisión (estrictamente futura)", example = "2027-01-15")
    private LocalDate fechaRevision;

    // 8. Confirmación booleana obligatoria
    @AssertTrue(message = "Debe aceptar la declaración de riesgo cripto marcando el campo como true")
    @Schema(description = "Declaración explícita de riesgo aceptado (debe ser true)", example = "true")
    private Boolean riesgoAceptado;

    // 9. Colección no vacía
    @NotEmpty(message = "Debe añadir al menos una etiqueta/tag para categorizar el activo")
    @Schema(description = "Lista de etiquetas para categorización (mínimo una)", example = "[\"L1\", \"STORE_OF_VALUE\"]")
    private List<String> etiquetas;
}
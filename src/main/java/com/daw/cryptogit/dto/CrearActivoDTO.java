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
public class CrearActivoDTO {
    // 1. Regex: De 2 a 10 letras MAYÚSCULAS estrictas (ej. BTC, ETH)
    @NotBlank(message = "El símbolo es obligatorio")
    @Pattern(regexp = "^[a-zA-Z]{2,10}$", message = "El símbolo debe contener entre 2 y 10 letras MAYÚSCULAS")
    private String simbolo;

    // 2. Tamaño entre 3 y 50 caracteres
    @NotBlank(message = "El nombre del activo es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    private String nombre;

    // 3. Control de precisión decimal (máx 8 enteros y 8 decimales)
    @NotNull(message = "La cantidad no puede ser nula")
    @Positive(message = "La cantidad debe ser un número positivo mayor que cero")
    @Digits(integer = 8, fraction = 8, message = "La cantidad no puede superar los 8 enteros y 8 decimales")
    private Double cantidad;

    // 4. Suelo mínimo en el precio
    @NotNull(message = "El precio de compra es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio de compra debe ser de al menos 0.01 EUR")
    private Double precioCompraEur;

    // 5. Formato de correo electrónico válido
    @NotBlank(message = "El email del analista es obligatorio")
    @Email(message = "Debe proporcionar una dirección de email válida (ej. analista@crypto.com)")
    private String emailAnalista;

    // 6. Fecha pasada o presente
    @NotNull(message = "La fecha de adquisición es obligatoria")
    @PastOrPresent(message = "La fecha de adquisición no puede estar en el futuro")
    private LocalDate fechaAdquisicion;

    // 7. Fecha estrictamente futura
    @NotNull(message = "La fecha de revisión no puede ser nula")
    @Future(message = "La fecha de revisión debe ser una fecha FUTURA")
    private LocalDate fechaRevision;

    // 8. Confirmación booleana obligatoria
    @AssertTrue(message = "Debe aceptar la declaración de riesgo cripto marcando el campo como true")
    private boolean riesgoAceptado;

    // 9. Colección no vacía
    @NotEmpty(message = "Debe añadir al menos una etiqueta/tag para categorizar el activo")
    private List<String> etiquetas;
}

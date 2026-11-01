package com.daw.cryptogit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de salida con los datos completos del activo almacenado en la cartera")
public class ActivoCryptoDTO {

    @Schema(description = "ID único autogenerado del activo", example = "1")
    private Long id;

    @Schema(description = "Símbolo/Ticker identificador en CoinGecko", example = "bitcoin")
    private String simbolo;

    @Schema(description = "Nombre completo del activo cripto", example = "Bitcoin")
    private String nombre;

    @Schema(description = "Cantidad del activo disponible en cartera", example = "0.5")
    private Double cantidad;

    @Schema(description = "Precio unitario de compra original en euros", example = "55000.00")
    private Double precioCompraEur;

    @Schema(description = "Correo electrónico del analista responsable", example = "analista@crypto.com")
    private String emailAnalista;

    @Schema(description = "Fecha de adquisición del activo", example = "2026-01-15")
    private LocalDate fechaAdquisicion;

    @Schema(description = "Fecha prevista para la revisión del activo", example = "2027-01-15")
    private LocalDate fechaRevision;

    @Schema(description = "Confirmación de aceptación de la declaración de riesgo", example = "true")
    private Boolean riesgoAceptado;

    @Schema(description = "Lista de etiquetas o categorías asignadas", example = "[\"L1\", \"STORE_OF_VALUE\"]")
    private List<String> etiquetas;
}
package com.daw.cryptogit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO Mashup híbrido que combina la cantidad de la BD local con la cotización en tiempo real de CoinGecko")
public class CarteraValoradaDTO {

    @Schema(description = "Nombre del activo de la base de datos local", example = "Bitcoin")
    private String nombreCrypto;

    @Schema(description = "Cantidad del activo almacenada en la BD local", example = "0.5")
    private Double cantidad;

    @Schema(description = "Precio unitario actual en euros obtenido vía API externa (CoinGecko)", example = "62500.00")
    private Double precioUnitarioEur;

    @Schema(description = "Valoración total en euros calculada (cantidad * precioUnitarioEur)", example = "31250.00")
    private Double valorTotalEur;

    @Schema(description = "Fuente de la información y detalle del cálculo o comisiones aplicadas", example = "Cotización en tiempo real vía CoinGecko API")
    private String fuente;
}

package com.daw.cryptogit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarteraValoradaDTO {
    private String nombreCrypto;
    private Double cantidad;
    private Double precioUnitarioEur;
    private Double valorTotalEur;
    private String fuente;
}

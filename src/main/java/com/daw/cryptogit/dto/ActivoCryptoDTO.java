package com.daw.cryptogit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivoCryptoDTO {

    private Long id;
    private String simbolo;
    private String nombre;
    private Double cantidad;
    private Double precioCompraEur;
    private String emailAnalista;
    private LocalDate fechaAdquisicion;
    private LocalDate fechaRevision;
    private Boolean riesgoAceptado;
    private List<String> etiquetas;
}

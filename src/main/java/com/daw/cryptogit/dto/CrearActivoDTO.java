package com.daw.cryptogit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearActivoDTO {
    private String simbolo;
    private String nombre;
    private Double cantidad;
}

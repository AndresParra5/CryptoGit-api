package com.daw.cryptogit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivoCryptoDTO {
    private Long id;
    private String simbolo;
    private String nombre;
    private Double cantidad;
}

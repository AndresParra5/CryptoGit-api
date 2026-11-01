package com.daw.cryptogit.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "activos_crypto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivoCrypto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String simbolo; // Ej: "bitcoin", "ethereum"

    @Column(nullable = false)
    private String nombre;  // Ej: "Bitcoin"

    @Column(nullable = false)
    private Double cantidad; // Ej: 0.5

    // Nuevos campos sin anotaciones @Column adicionales
    private Double precioCompraEur;
    private String emailAnalista;
    private LocalDate fechaAdquisicion;
    private LocalDate fechaRevision;
    private boolean riesgoAceptado;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> etiquetas;

    // CONSTRUCTOR SOBRECARGADO
    public ActivoCrypto(String simbolo, String nombre, Double cantidad, Double precioCompraEur) {
        this.simbolo = simbolo;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precioCompraEur = precioCompraEur;
    }
}

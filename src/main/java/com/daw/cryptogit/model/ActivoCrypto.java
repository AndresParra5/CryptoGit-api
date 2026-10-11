package com.daw.cryptogit.model;

import jakarta.persistence.*;
import lombok.*;

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
}

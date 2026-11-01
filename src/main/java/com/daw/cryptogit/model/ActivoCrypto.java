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

    // 1. Clave candidata única en mayúsculas/minúsculas y no nula (ej. "bitcoin", "ethereum")
    @Column(nullable = false, unique = true, length = 10)
    private String simbolo;

    // 2. Nombre del activo obligatorio con límite de caracteres
    @Column(nullable = false, length = 50)
    private String nombre;

    // 3. Cantidad no nula en la base de datos
    @Column(nullable = false)
    private Double cantidad;

    // 4. Precio de compra original en Euros
    @Column(name = "precio_compra_eur", nullable = false)
    private Double precioCompraEur;

    // 5. Correo del analista responsable del registro
    @Column(name = "email_analista", nullable = false, length = 100)
    private String emailAnalista;

    // 6. Fecha de adquisición del activo (Formato ISO en BD)
    @Column(name = "fecha_adquisicion", nullable = false)
    private LocalDate fechaAdquisicion;

    // 7. Fecha programada para revisión o venta
    @Column(name = "fecha_revision", nullable = false)
    private LocalDate fechaRevision;

    // 8. Confirmación booleana legal de aceptación de riesgos
    @Column(name = "riesgo_aceptado", nullable = false)
    private boolean riesgoAceptado;

    // 9. Colección de etiquetas simples guardadas en tabla secundaria auxiliar
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "activo_crypto_etiquetas", joinColumns = @JoinColumn(name = "activo_crypto_id"))
    @Column(name = "etiqueta")
    private List<String> etiquetas;

    // Constructor sobrecargado para inicialización rápida en pruebas
    public ActivoCrypto(String simbolo, String nombre, Double cantidad, Double precioCompraEur) {
        this.simbolo = simbolo;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precioCompraEur = precioCompraEur;
    }
}
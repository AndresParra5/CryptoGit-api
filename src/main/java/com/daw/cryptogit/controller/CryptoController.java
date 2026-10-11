package com.daw.cryptogit.controller;

import com.daw.cryptogit.dto.CarteraValoradaDTO;
import com.daw.cryptogit.model.ActivoCrypto;
import com.daw.cryptogit.service.CryptoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/crypto")
@Tag(name = "CryptoTrader API", description = "Gestión de Cartera Mashup con Cotizaciones en Tiempo Real")
public class CryptoController {

    private final CryptoService service;

    @Value("${app.entorno.nombre:DESCONOCIDO}")
    private String nombreEntorno;

    public CryptoController(CryptoService service) {
        this.service = service;
    }

    @GetMapping("/entorno")
    @Operation(summary = "Obtener el entorno de ejecución activo")
    public ResponseEntity<String> obtenerEntorno() {
        return ResponseEntity.ok("Conectado al entorno: " + nombreEntorno);
    }

    @GetMapping("/activos")
    @Operation(summary = "Listar todos los activos de la base de datos local H2")
    public ResponseEntity<List<ActivoCrypto>> listarActivos() {
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @PostMapping("/activos")
    @Operation(summary = "Registrar un nuevo activo crypto (Limpia la Caché)")
    public ResponseEntity<ActivoCrypto> crearActivo(@RequestBody ActivoCrypto activo) {
        ActivoCrypto nuevo = service.guardarActivo(activo);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @GetMapping("/activos/{id}/valorar")
    @Operation(summary = "Obtener valoración Mashup combinando H2 + CoinGecko API (Aplica Caché)")
    public ResponseEntity<CarteraValoradaDTO> valorarActivo(@PathVariable Long id) {
        return ResponseEntity.ok(service.valorarActivo(id));
    }
}
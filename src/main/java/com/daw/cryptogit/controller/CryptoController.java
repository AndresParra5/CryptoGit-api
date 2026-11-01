package com.daw.cryptogit.controller;

import com.daw.cryptogit.dto.ActivoCryptoDTO;
import com.daw.cryptogit.dto.CarteraValoradaDTO;
import com.daw.cryptogit.dto.CrearActivoDTO;
import com.daw.cryptogit.dto.PatchActivoDTO;
import com.daw.cryptogit.model.ActivoCrypto;
import com.daw.cryptogit.service.CryptoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/crypto")
@Tag(name = "CryptoTrader API", description = "Gestión de Cartera Mashup con Cotizaciones en Tiempo Real")
@RequiredArgsConstructor // Lombok genera el constructor para los campos 'final' al compilar
public class CryptoController {

    private final CryptoService service;

    @Value("${app.entorno.nombre:DESCONOCIDO}")
    private String nombreEntorno;

    //public CryptoController(CryptoService service) {this.service = service;}

    @GetMapping("/entorno")
    @Operation(summary = "Obtener el entorno de ejecución activo")
    public ResponseEntity<String> obtenerEntorno() {
        return ResponseEntity.ok("Conectado al entorno: " + nombreEntorno);
    }

    @GetMapping("/activos")
    @Operation(summary = "Listar todos los activos de la base de datos local H2")
    public ResponseEntity<List<ActivoCryptoDTO>> listarActivos() {
        return ResponseEntity.ok(service.obtenerTodos());
    }

    // POST /api/crypto/activos
// Al poner @Valid, Spring valida TODAS las reglas definidas en ActivoCreacionDTO
    @PostMapping("/activos")
    public ResponseEntity<ActivoCryptoDTO> crearActivo(@Valid @RequestBody CrearActivoDTO dto) {
        ActivoCryptoDTO nuevo = service.guardarActivo(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @GetMapping("/activos/{id}/valorar")
    @Operation(summary = "Obtener valoración Mashup combinando H2 + CoinGecko API (Aplica Caché)")
    public ResponseEntity<CarteraValoradaDTO> valorarActivo(@PathVariable Long id) {
        return ResponseEntity.ok(service.valorarActivo(id));
    }

    /**
     * COMBINACIÓN DE PARÁMETROS HTTP:
     * 1. @PathVariable("id")              -> Obligatorio en la ruta URL (/activos/{id}/...)
     * 2. @RequestParam(defaultValue="2.0") -> Opcional tras el ? (si no se envía, toma 2.0 por defecto)
     */
    @GetMapping("/activos/{id}/valorar-comision")
    public ResponseEntity<CarteraValoradaDTO> valorarConComision(
            @PathVariable Long id,
            @RequestParam(defaultValue = "2.0") double porcentaje) {
        return ResponseEntity.ok(service.valorarActivoConComision(id, porcentaje));
    }

    // GET http://localhost:8080/api/crypto/activos/1
    @GetMapping("/activos/{id}")
    public ResponseEntity<ActivoCryptoDTO> obtenerPorId(@PathVariable Long id) {
        ActivoCryptoDTO dto = service.obtenerPorId(id);
        return ResponseEntity.ok(dto);
    }

    // GET http://localhost:8080/api/crypto/activos/buscar?simbolo=btc
    @GetMapping("/activos/buscar")
    public ResponseEntity<ActivoCryptoDTO> obtenerPorSimbolo(@RequestParam String simbolo) {
        ActivoCryptoDTO dto = service.obtenerPorSimbolo(simbolo);
        return ResponseEntity.ok(dto);
    }

    // PUT /activos/{id} -> Actualización completa usando CrearActivoDTO
    @PutMapping("/activos/{id}")
    public ResponseEntity<ActivoCryptoDTO> actualizarCompleto(@PathVariable Long id, @Valid @RequestBody CrearActivoDTO dto) {
        return ResponseEntity.ok(service.actualizarCompleto(id, dto));
    }

    // PATCH /activos/{id} -> Actualización parcial usando PatchActivoDTO
    @PatchMapping("/activos/{id}")
    public ResponseEntity<ActivoCryptoDTO> actualizarParcial(@PathVariable Long id, @Valid @RequestBody PatchActivoDTO dto) {
        return ResponseEntity.ok(service.actualizarParcial(id, dto));
    }

    // DELETE /activos/{id} -> Borrado por ID
    @DeleteMapping("/activos/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
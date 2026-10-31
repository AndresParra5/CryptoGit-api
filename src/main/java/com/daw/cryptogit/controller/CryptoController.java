package com.daw.cryptogit.controller;

import com.daw.cryptogit.dto.ActivoCryptoDTO;
import com.daw.cryptogit.dto.CarteraValoradaDTO;
import com.daw.cryptogit.dto.CrearActivoDTO;
import com.daw.cryptogit.model.ActivoCrypto;
import com.daw.cryptogit.service.CryptoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @PostMapping("/activos")
    @Operation(summary = "Registrar un nuevo activo crypto (Limpia la Caché)")
    public ResponseEntity<ActivoCryptoDTO> crearActivo(@RequestBody CrearActivoDTO activo) {
        ActivoCryptoDTO nuevo = service.guardarActivo(activo);
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
            @PathVariable Long id,						// Captura la variable de la URL
            @RequestParam(defaultValue = "2.0") double porcentaje) {	// Captura el Query Param opcional

        try {
            // Delegamos la lógica completa al servicio
            CarteraValoradaDTO resultado = service.valorarActivoConComision(id, porcentaje);
            return ResponseEntity.ok(resultado); // Devuelve HTTP 200 OK con el DTO
        } catch (IllegalArgumentException e) {
            // // Si el servicio lanza la excepción del rango (menor a 0 o mayor a 50), devolvemos un 400 Bad Request
            return ResponseEntity.badRequest().build();
        }
    }
}
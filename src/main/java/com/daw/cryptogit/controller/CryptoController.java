package com.daw.cryptogit.controller;

import com.daw.cryptogit.dto.ActivoCryptoDTO;
import com.daw.cryptogit.dto.CarteraValoradaDTO;
import com.daw.cryptogit.dto.CrearActivoDTO;
import com.daw.cryptogit.dto.PatchActivoDTO;
import com.daw.cryptogit.model.ActivoCrypto;
import com.daw.cryptogit.service.CryptoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Obtener el entorno de ejecución activo", description = "Muestra el perfil activo de la aplicación (dev, test, prod).")
    @ApiResponse(responseCode = "200", description = "Entorno devuelto correctamente")
    public ResponseEntity<String> obtenerEntorno() {
        return ResponseEntity.ok("Conectado al entorno: " + nombreEntorno);
    }

    @GetMapping("/activos")
    @Operation(summary = "Listar todos los activos de la base de datos local H2", description = "Devuelve el listado completo de activos almacenados.")
    @ApiResponse(responseCode = "200", description = "Lista de activos devuelta con éxito")
    public ResponseEntity<List<ActivoCryptoDTO>> listarActivos() {
        return ResponseEntity.ok(service.obtenerTodos());
    }

    // POST /api/crypto/activos
// Al poner @Valid, Spring valida TODAS las reglas definidas en ActivoCreacionDTO
    @PostMapping("/activos")
    @Operation(summary = "Registrar un nuevo activo cripto", description = "Crea un activo tras validar el DTO de entrada e invalida la caché de cotizaciones.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Activo creado con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos (falso Bean Validation)")
    })
    public ResponseEntity<ActivoCryptoDTO> crearActivo(@Valid @RequestBody CrearActivoDTO dto) {
        ActivoCryptoDTO nuevo = service.guardarActivo(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @GetMapping("/activos/{id}/valorar")
    @Operation(summary = "Obtener valoración Mashup combinando H2 + CoinGecko API (Aplica Caché)", description = "Consulta la BD local y llama a CoinGecko para calcular el valor total. Almacena en caché en RAM.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Valoración calculada con éxito"),
            @ApiResponse(responseCode = "404", description = "Activo no encontrado con el ID proporcionado")
    })
    public ResponseEntity<CarteraValoradaDTO> valorarActivo(
            @Parameter(description = "ID del activo a valorar", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(service.valorarActivo(id));
    }

    /**
     - COMBINACIÓN DE PARÁMETROS HTTP:

     - @PathVariable("id")              -> Obligatorio en la ruta URL (/activos/{id}/...)

     - 2. @RequestParam(defaultValue="2.0") -> Opcional tras el ? (si no se envía, toma 2.0 por defecto)

     */
    @GetMapping("/activos/{id}/valorar-comision")
    @Operation(summary = "Obtener valoración aplicando comisión personalizada", description = "Calcula el valor total aplicando una comisión (entre 0% y 50%). Reutiliza la cotización cacheada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Valoración con comisión calculada con éxito"),
            @ApiResponse(responseCode = "400", description = "Porcentaje de comisión fuera del rango permitido (0-50%)"),
            @ApiResponse(responseCode = "404", description = "Activo no encontrado")
    })
    public ResponseEntity<CarteraValoradaDTO> valorarConComision(
            @Parameter(description = "ID del activo a valorar", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Porcentaje de comisión a aplicar (0 a 50)", example = "2.0")
            @RequestParam(defaultValue = "2.0") double porcentaje) {
        return ResponseEntity.ok(service.valorarActivoConComision(id, porcentaje));
    }

    // GET http://localhost:8080/api/crypto/activos/1
    @GetMapping("/activos/{id}")
    @Operation(summary = "Buscar un activo por su ID", description = "Obtiene el activo almacenado en la BD local correspondiente al ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activo encontrado"),
            @ApiResponse(responseCode = "404", description = "No existe ningún activo con el ID proporcionado")
    })
    public ResponseEntity<ActivoCryptoDTO> obtenerPorId(
            @Parameter(description = "ID del activo", example = "1")
            @PathVariable Long id) {
        ActivoCryptoDTO dto = service.obtenerPorId(id);
        return ResponseEntity.ok(dto);
    }

    // GET http://localhost:8080/api/crypto/activos/buscar?simbolo=btc
    @GetMapping("/activos/buscar")
    @Operation(summary = "Buscar un activo por su símbolo", description = "Busca en la BD local un activo coincidente con el símbolo (ej. btc, eth).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activo encontrado"),
            @ApiResponse(responseCode = "404", description = "No existe ningún activo con el símbolo indicado")
    })
    public ResponseEntity<ActivoCryptoDTO> obtenerPorSimbolo(
            @Parameter(description = "Símbolo/Ticker del activo", example = "btc")
            @RequestParam String simbolo) {
        ActivoCryptoDTO dto = service.obtenerPorSimbolo(simbolo);
        return ResponseEntity.ok(dto);
    }

    // PUT /activos/{id} -> Actualización completa usando CrearActivoDTO
    @PutMapping("/activos/{id}")
    @Operation(summary = "Reemplazar un activo completamente (PUT)", description = "Sustituye todas las propiedades del activo existente e invalida la caché.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activo actualizado con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos del DTO de entrada no válidos"),
            @ApiResponse(responseCode = "404", description = "Activo no encontrado")
    })
    public ResponseEntity<ActivoCryptoDTO> actualizarCompleto(
            @Parameter(description = "ID del activo a reemplazar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody CrearActivoDTO dto) {
        return ResponseEntity.ok(service.actualizarCompleto(id, dto));
    }

    // PATCH /activos/{id} -> Actualización parcial usando PatchActivoDTO
    @PatchMapping("/activos/{id}")
    @Operation(summary = "Modificar campos de un activo parcialmente (PATCH)", description = "Actualiza únicamente los campos enviados en la petición e invalida la caché.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activo modificado parcialmente con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos de modificación no válidos"),
            @ApiResponse(responseCode = "404", description = "Activo no encontrado")
    })
    public ResponseEntity<ActivoCryptoDTO> actualizarParcial(
            @Parameter(description = "ID del activo a modificar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody PatchActivoDTO dto) {
        return ResponseEntity.ok(service.actualizarParcial(id, dto));
    }

    // DELETE /activos/{id} -> Borrado por ID
    @DeleteMapping("/activos/{id}")
    @Operation(summary = "Eliminar un activo por ID", description = "Elimina permanentemente el activo de la BD local e invalida la caché de cotizaciones.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Activo eliminado con éxito (sin contenido en respuesta)"),
            @ApiResponse(responseCode = "404", description = "Activo no encontrado")
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del activo a eliminar", example = "1")
            @PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
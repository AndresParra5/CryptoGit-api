package com.daw.cryptogit.service;

import com.daw.cryptogit.dto.ActivoCryptoDTO;
import com.daw.cryptogit.dto.CarteraValoradaDTO;
import com.daw.cryptogit.dto.CoinGeckoResponseDTO;
import com.daw.cryptogit.dto.CrearActivoDTO;
import com.daw.cryptogit.model.ActivoCrypto;
import com.daw.cryptogit.repository.ActivoCryptoRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

@Service
public class CryptoService {

    private final ActivoCryptoRepository repository;
    private final RestClient restClient;


    public CryptoService(ActivoCryptoRepository repository, RestClient restClient) {
        this.repository = repository;
        this.restClient = restClient;
    }

    // Metodo privado auxiliar en CryptoService
    private ActivoCryptoDTO convertirADto(ActivoCrypto activo) {
        return new ActivoCryptoDTO(
                activo.getId(),
                activo.getSimbolo(),
                activo.getNombre(),
                activo.getCantidad(),
                activo.getPrecioCompraEur(),
                activo.getEmailAnalista(),
                activo.getFechaAdquisicion(),
                activo.getFechaRevision(),
                activo.isRiesgoAceptado(),
                activo.getEtiquetas()
        );
    }

    // Convertir lista de entidades a DTOs de salida
    public List<ActivoCryptoDTO> obtenerTodos() {
        return repository.findAll().stream()
                .map(this::convertirADto)
                .toList();
    }

    // Convertir DTO de entrada a Entidad -> Guardar -> Devolver DTO de salida
    @CacheEvict(value = "cotizaciones", allEntries = true)
    public ActivoCryptoDTO guardarActivo(CrearActivoDTO dto) {
        ActivoCrypto activo = new ActivoCrypto();
        activo.setSimbolo(dto.getSimbolo().toLowerCase());
        activo.setNombre(dto.getNombre());
        activo.setCantidad(dto.getCantidad());
        activo.setPrecioCompraEur(dto.getPrecioCompraEur());
        activo.setEmailAnalista(dto.getEmailAnalista());
        activo.setFechaAdquisicion(dto.getFechaAdquisicion());
        activo.setFechaRevision(dto.getFechaRevision());
        activo.setRiesgoAceptado(dto.isRiesgoAceptado());
        activo.setEtiquetas(dto.getEtiquetas());

        ActivoCrypto guardado = repository.save(activo);

        return convertirADto(guardado);
    }

    @Cacheable(value = "cotizaciones", key = "#id")
    public CarteraValoradaDTO valorarActivo(Long id) {
        System.out.println("--> [SERVICIO REAL] Petición externa a CoinGecko API para activo ID: " + id);

        ActivoCrypto activo = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activo no encontrado con ID: " + id));

        // Petición HTTP a la API externa de CoinGecko
        String url = String.format("https://api.coingecko.com/api/v3/simple/price?ids=%s&vs_currencies=eur",
                activo.getSimbolo());

        CoinGeckoResponseDTO response = restClient.get()
                .uri(url)
                .retrieve()
                .body(CoinGeckoResponseDTO.class);

        Double precioEur = (response != null) ? response.getPrecioEur(activo.getSimbolo()) : 0.0;
        Double valorTotal = activo.getCantidad() * precioEur;

        return new CarteraValoradaDTO(
                activo.getNombre(),
                activo.getCantidad(),
                precioEur,
                valorTotal,
                "Cotización en tiempo real vía CoinGecko API"
        );
    }

    /**
     * Lógica de negocio: Valida el rango de comisión y actualiza la valoración del activo.
     */
    public CarteraValoradaDTO valorarActivoConComision(Long id, double porcentajeComision) {

        // VALIDACIÓN DE RANGOS (Regla de negocio condicional):
        // Si la comisión es menor a 0 o mayor a 50%, lanzamos una excepción de error.
        if (porcentajeComision < 0 || porcentajeComision > 50) {
            throw new IllegalArgumentException("Error: La comisión debe estar entre 0% y 50%.");
        }

        // Llamamos al metodo valorarActivo(id) para obtener los datos base.
        CarteraValoradaDTO dto = valorarActivo(id);

        // AÑADIMOS EL CÁLCULO DE LA COMISIÓN:
        // Si hay comisión, recalculamos el valor total en euros una sola vez.
        if (porcentajeComision > 0) {
            double valorConComision = dto.getValorTotalEur() * (1 + porcentajeComision / 100);
            dto.setValorTotalEur(valorConComision);
        }

        // AÑADIMOS MENSAJES DINÁMICOS/CONDICIONALES:
        // Concatenamos un texto explicativo en el campo "fuente" del DTO según el tramo de la comisión.
        if (porcentajeComision == 0) {
            dto.setFuente(dto.getFuente() + " | Operación libre de comisiones");
        } else if (porcentajeComision > 10) {
            dto.setFuente(dto.getFuente() + " | ¡Aviso! Comisión alta aplicada: " + porcentajeComision + "%");
        } else {
            dto.setFuente(dto.getFuente() + " | Comisión estándar: " + porcentajeComision + "%");
        }

        return dto;
    }

    // Buscar activo por su ID en la BD
    public Optional<ActivoCryptoDTO> obtenerPorId(Long id) {
        return repository.findById(id)
                .map(this::convertirADto);
    }

    // Buscar activo por símbolo (ej: ?simbolo=btc)
    public Optional<ActivoCryptoDTO> obtenerPorSimbolo(String simbolo) {
        return repository.findBySimbolo(simbolo.toLowerCase())
                .map(this::convertirADto);
    }
}
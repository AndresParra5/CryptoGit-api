package com.daw.cryptogit.service;

import com.daw.cryptogit.dto.CarteraValoradaDTO;
import com.daw.cryptogit.dto.CoinGeckoResponseDTO;
import com.daw.cryptogit.model.ActivoCrypto;
import com.daw.cryptogit.repository.ActivoCryptoRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class CryptoService {

    private final ActivoCryptoRepository repository;
    private final RestClient restClient;


    public CryptoService(ActivoCryptoRepository repository, RestClient restClient) {
        this.repository = repository;
        this.restClient = restClient;
    }

    public List<ActivoCrypto> obtenerTodos() {
        return repository.findAll();
    }

    @CacheEvict(value = "cotizaciones", allEntries = true)
    public ActivoCrypto guardarActivo(ActivoCrypto activo) {
        // Normalizamos el símbolo a minúsculas
        activo.setSimbolo(activo.getSimbolo().toLowerCase());
        return repository.save(activo);
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
}
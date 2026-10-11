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
}
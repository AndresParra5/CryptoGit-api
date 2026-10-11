package com.daw.cryptogit.dto;

import java.util.Map;

// Mapea la estructura dinámica que devuelve la API de CoinGecko
public class CoinGeckoResponseDTO extends java.util.HashMap<String, Map<String, Double>> {

    public Double getPrecioEur(String simbolo) {
        if (this.containsKey(simbolo) && this.get(simbolo).containsKey("eur")) {
            return this.get(simbolo).get("eur");
        }
        return 0.0;
    }
}

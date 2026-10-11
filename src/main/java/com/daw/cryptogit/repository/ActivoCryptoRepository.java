package com.daw.cryptogit.repository;

import com.daw.cryptogit.model.ActivoCrypto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActivoCryptoRepository extends JpaRepository<ActivoCrypto, Long> {
    Optional<ActivoCrypto> findBySimbolo(String simbolo);
}

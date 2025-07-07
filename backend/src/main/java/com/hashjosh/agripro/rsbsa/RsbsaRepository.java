package com.hashjosh.agripro.rsbsa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RsbsaRepository extends JpaRepository<RsbsaModel, Long> {

    Optional<RsbsaModel> findByRsbsaIdEqualsIgnoreCase(String rsbsaId);
}

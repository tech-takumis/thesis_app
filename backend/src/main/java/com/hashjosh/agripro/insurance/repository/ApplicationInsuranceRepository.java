package com.hashjosh.agripro.insurance.repository;

import com.hashjosh.agripro.insurance.models.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationInsuranceRepository extends JpaRepository<Application, Long> {

    List<Application> findAllByUserId(Long userId);

}

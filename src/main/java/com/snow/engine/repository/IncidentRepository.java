package com.snow.engine.repository;

import com.snow.engine.entity.Incident;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IncidentRepository extends JpaRepository<Incident,String> {
    Optional<Incident> findByNumber(String number);
}

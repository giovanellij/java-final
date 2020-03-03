package com.java.alquiler.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.java.alquiler.entities.Servicio;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long>{

}
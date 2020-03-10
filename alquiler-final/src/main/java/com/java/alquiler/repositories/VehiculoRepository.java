package com.java.alquiler.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.java.alquiler.entities.Vehiculo;


@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long>{
	
	@Query("select v from Vehiculo v where v.alquilado = false and (v.descripcion like %:searchText% OR v.patente like %:searchText%)")
    List<Vehiculo> findDisponiblesBySearchText(@Param("searchText")  String searchText);
	
	@Query("select v from Vehiculo v where v.alquilado = true and (v.descripcion like %:searchText% OR v.patente like %:searchText%)")
    List<Vehiculo> findPendientesDevolucionBySearchText(@Param("searchText") String searchText);
	
	@Query("select v from Vehiculo v where (v.descripcion like %:searchText% OR v.patente like %:searchText%)")
    List<Vehiculo> findAllBySearchText(@Param("searchText") String searchText);
	
	
	@Query("select v from Vehiculo v where v.alquilado = false")
    List<Vehiculo> findAllVehiculosDisponibles();
	
	@Query("select v from Vehiculo v where v.alquilado = true")
    List<Vehiculo> findAllVehiculosAlquiladosByCliente(@Param("clienteId") long clienteId);
}

// and v.id in (select s.vehiculoId from Servicio s where s.fecServicio >= ?1 and s.fecServicio <= ?2)
//and v.id in (select s.vehiculoId from Servicio s where s.fecServicio >= 1551373742 and s.fecServicio <= 1592909742)+?
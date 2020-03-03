package com.java.alquiler.repositories;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.java.alquiler.entities.Vehiculo;


@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long>{
	
//	@Query("select v from Vehiculo v where v.alquilado = false and (v.descripcion like %?3% OR v.patente like %?3%) and v.id in (select s.vehiculoId from Servicio s where s.fecServicio >= ?1 and s.fecServicio <= ?2)")
//    List<Vehiculo> findDisponiblesBetweenDatesAndSearchText(
//    		String from, 
//    		String to,
//    		@Param("searchText")  String searchText);
//	
//	@Query("select v from Vehiculo v where v.alquilado = true and (v.descripcion like %:searchText% OR v.patente like %:searchText%) and v.id in (select s.vehiculoId from Servicio s where s.fecServicio >= ?1 and s.fecServicio <= ?2)")
//    List<Vehiculo> findPendientesDevolucionBetweenDatesAndSearchText(
//    		String from, 
//    		String to,
//    		@Param("searchText") String searchText);
	
	@Query("select v from Vehiculo v where (v.descripcion like %:searchText% OR v.patente like %:searchText%)")
    List<Vehiculo> findAllBetweenDatesAndSearchText(
//    		@Param("fromDate")Timestamp from, 
//    		@Param("toDate")Timestamp to,
    		@Param("searchText") String searchText);
	
	
	@Query("select v from Vehiculo v where v.alquilado = false")
    List<Vehiculo> findAllVehiculosDisponibles();
	

}

// and v.id in (select s.vehiculoId from Servicio s where s.fecServicio >= ?1 and s.fecServicio <= ?2)
//and v.id in (select s.vehiculoId from Servicio s where s.fecServicio >= 1551373742 and s.fecServicio <= 1592909742)+?
package com.java.alquiler.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.java.alquiler.entities.Servicio;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long>{
	@Query("select s from Servicio s where s.vehiculoId = :vehiculoId AND s.fecCancelacion = null")
    Optional<Servicio> findServicioActivoByVehiculoId(@Param("vehiculoId") long vehiculoId);
	
	@Query("select s from Servicio s where s.clienteId = :clienteId order by s.fecServicio desc")
	List<Servicio> findAllServiciosByClienteId(long clienteId);
	
	@Query("select s from Servicio s order by s.fecServicio desc")
	List<Servicio> findAllServicios();
	
	@Query("select s from Servicio s where (s.fecCancelacion = null) order by s.fecServicio desc")
	List<Servicio> findServiciosAlquilados();
	
	@Query("select s from Servicio s where (s.fecCancelacion != null) order by s.fecServicio desc")
	List<Servicio> findServiciosDevueltos();

}
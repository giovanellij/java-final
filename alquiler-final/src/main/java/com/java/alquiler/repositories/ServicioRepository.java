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
	
	@Query("select s from Servicio s where (s.fecServicio between :fromDate and :toDate) order by s.fecServicio desc")
	List<Servicio> findAllServiciosBetweenDates(@Param("fromDate") String fromDate, @Param("toDate") String toDate);
	
	@Query("select s from Servicio s where (s.fecCancelacion = null and (s.fecServicio between :fromDate and :toDate)) order by s.fecServicio desc")
	List<Servicio> findServiciosAlquiladosBetweenDates(@Param("fromDate") String fromDate, @Param("toDate") String toDate);
	
	@Query("select s from Servicio s where (s.fecCancelacion != null and (s.fecServicio between :fromDate and :toDate)) order by s.fecServicio desc")
	List<Servicio> findServiciosDevueltosBetweenDates(@Param("fromDate") String fromDate, @Param("toDate") String toDate);
}
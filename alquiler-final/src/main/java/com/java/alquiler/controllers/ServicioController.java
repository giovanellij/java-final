package com.java.alquiler.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java.alquiler.business.CreateServicio;
import com.java.alquiler.business.SearchServicioCriteria;
import com.java.alquiler.entities.Servicio;
import com.java.alquiler.entities.Usuario;
import com.java.alquiler.entities.Vehiculo;
import com.java.alquiler.exceptions.DateBeforeTodayException;
import com.java.alquiler.exceptions.RequestUnauthorizedException;
import com.java.alquiler.exceptions.ResourceNotFoundException;
import com.java.alquiler.repositories.ServicioRepository;
import com.java.alquiler.repositories.UserRepository;
import com.java.alquiler.repositories.VehiculoRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1")
public class ServicioController {
	@Autowired
	private ServicioRepository servicioRepository;
	
	@Autowired
	private UserRepository usuarioRepository;
	
	@Autowired
	private VehiculoRepository vehiculoRepository;

	@GetMapping("/servicios")
	public List<Servicio> getAllServicios() {
		return servicioRepository.findAll();
	}
	
	@GetMapping("/serviciosByCliente/{id}")
	public List<Servicio> getAllServicios(@PathVariable(value = "id") Long clienteId) {
		return servicioRepository.findAllServiciosByClienteId(clienteId);
	}

	@GetMapping("/servicio/{id}")
	public ResponseEntity<Servicio> getServicioById(@PathVariable(value = "id") Long servicioId)
			throws ResourceNotFoundException {
		Servicio servicio = servicioRepository.findById(servicioId)
				.orElseThrow(() -> new ResourceNotFoundException("Servicio not found for this id : " + servicioId));
		return ResponseEntity.ok().body(servicio);
	}
	
	@PostMapping("/serviciosFiltered")
	public List<Servicio> getServiciosByFilter(@Valid @RequestBody SearchServicioCriteria criteria) throws ParseException, ResourceNotFoundException, RequestUnauthorizedException {
		
		Usuario usuario = usuarioRepository.findByUserName(criteria.searchBy)
				.orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado un usuario para ese nombre: " + criteria.searchBy));
		
		if(!usuario.isAdministrador()) {			
			throw new RequestUnauthorizedException("El usuario no tiene permiso para realizar esta accion");			
		}
		
		List<Servicio> servicios = new ArrayList<Servicio>();
		
		Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(criteria.getFromDate()); 
		Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse(criteria.getToDate());
		
		if(criteria.alquileres && criteria.devoluciones) {
			servicios = servicioRepository
					.findAll()
					.stream()
					.filter(p -> p.getFecServicio().after(fromDate) && p.getFecServicio().before(toDate))
					.collect(Collectors.toList());
		} else {
			if(criteria.alquileres) {
				servicios = servicioRepository
						.findServiciosAlquilados()
						.stream()
						.filter(p -> p.getTimestampServicio().after(fromDate) && p.getTimestampServicio().before(toDate))
						.collect(Collectors.toList());
			} else {
				servicios = servicioRepository
						.findServiciosDevueltos()
						.stream()
						.filter(p -> p.getTimestampServicio().after(fromDate) && p.getTimestampServicio().before(toDate))
						.collect(Collectors.toList());
			}
		}
		
		return servicios;
	}

	@PostMapping("/servicios")
	public Servicio createServicio(@RequestBody CreateServicio createServicio) throws ParseException, ResourceNotFoundException, DateBeforeTodayException {
		Servicio servicio = new Servicio();
		
		Date fecServicio = new SimpleDateFormat("yyyy-MM-dd").parse(createServicio.fecServicio);
		
		Date today = new Date();		
		
		if(fecServicio.compareTo(today) < 0) {
			throw new DateBeforeTodayException("La fecha seleccionada para el alquiler no puede ser menor a la de hoy.");
		}
		
		servicio.setNroReserva(createServicio.nroReserva);
		servicio.setFecServicio(fecServicio);
		servicio.setFecCancelacion(null);
		servicio.setClienteId(createServicio.clienteId);
		servicio.setVehiculoId(createServicio.vehiculoId);
		
		Vehiculo vehiculo = vehiculoRepository.findById(servicio.getVehiculoId()).orElseThrow(() -> new ResourceNotFoundException("Vehiculo not found for this id : " + servicio.getVehiculoId()));;
		
		vehiculo.setAlquilado(true);
		
		vehiculoRepository.save(vehiculo);
		
		return servicioRepository.save(servicio);
	}

	
	@PostMapping("/serviciosDevolver")
	public Servicio updateServicio(@RequestBody Vehiculo vehiculoParaDevolver) throws ResourceNotFoundException {
		
		Vehiculo vehiculo = vehiculoRepository.findById(vehiculoParaDevolver.getId())
				.orElseThrow(() -> new ResourceNotFoundException("No existe vehiculo para ese id : " + vehiculoParaDevolver.getId()));
		
		
		Servicio servicio = servicioRepository.findServicioActivoByVehiculoId(vehiculoParaDevolver.getId())
				.orElseThrow(() -> new ResourceNotFoundException("No existe servicio activo para ese vehiculo : " + vehiculoParaDevolver.getId()));

		servicio.setFecCancelacion(new Date());
		
		vehiculo.setAlquilado(false);
		
		vehiculoRepository.save(vehiculo);
		
		return servicioRepository.save(servicio);
	}
	
	@PostMapping("/serviciosDevolverByServicio")
	public Servicio devolver(@RequestBody Servicio servicio) throws ResourceNotFoundException {
		
		Vehiculo vehiculo = vehiculoRepository.findById(servicio.getVehiculoId())
				.orElseThrow(() -> new ResourceNotFoundException("No existe vehiculo para ese id : " + servicio.getVehiculoId()));
		
		
		Servicio servicioReturn = servicioRepository.findById(servicio.getId())
				.orElseThrow(() -> new ResourceNotFoundException("No existe servicio activo para ese id : " + servicio.getId()));

		servicioReturn.setFecCancelacion(new Date());
		
		vehiculo.setAlquilado(false);
		
		vehiculoRepository.save(vehiculo);
		
		return servicioRepository.save(servicioReturn);
	}

	@DeleteMapping("/servicios/{id}")
	public Map<String, Boolean> deleteServicio(@PathVariable(value = "id") Long servicioId)
			throws ResourceNotFoundException {
		Servicio servicio = servicioRepository.findById(servicioId)
				.orElseThrow(() -> new ResourceNotFoundException("Servicio not found for this id : " + servicioId));

		servicioRepository.delete(servicio);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}

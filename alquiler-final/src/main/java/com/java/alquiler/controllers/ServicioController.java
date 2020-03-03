package com.java.alquiler.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java.alquiler.business.CreateServicio;
import com.java.alquiler.entities.Servicio;
import com.java.alquiler.entities.Vehiculo;
import com.java.alquiler.exceptions.ResourceNotFoundException;
import com.java.alquiler.repositories.ServicioRepository;
import com.java.alquiler.repositories.VehiculoRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1")
public class ServicioController {
	@Autowired
	private ServicioRepository servicioRepository;
	
	@Autowired
	private VehiculoRepository vehiculoRepository;

	@GetMapping("/servicios")
	public List<Servicio> getAllServicios() {
		return servicioRepository.findAll();
	}

	@GetMapping("/servicio/{id}")
	public ResponseEntity<Servicio> getServicioById(@PathVariable(value = "id") Long servicioId)
			throws ResourceNotFoundException {
		Servicio servicio = servicioRepository.findById(servicioId)
				.orElseThrow(() -> new ResourceNotFoundException("Servicio not found for this id : " + servicioId));
		return ResponseEntity.ok().body(servicio);
	}

	@PostMapping("/servicios")
	public Servicio createServicio(@RequestBody CreateServicio createServicio) throws ParseException, ResourceNotFoundException {
		Servicio servicio = new Servicio();
		
		servicio.setNroReserva(createServicio.nroReserva);
		servicio.setFecServicio(new SimpleDateFormat("yyyy-MM-dd").parse(createServicio.fecServicio));
		servicio.setFecCancelacion(null);
		servicio.setClienteId(createServicio.clienteId);
		servicio.setVehiculoId(createServicio.vehiculoId);
		
		Vehiculo vehiculo = vehiculoRepository.findById(servicio.getVehiculoId()).orElseThrow(() -> new ResourceNotFoundException("Vehiculo not found for this id : " + servicio.getVehiculoId()));;
		
		vehiculo.setAlquilado(true);
		
		vehiculoRepository.save(vehiculo);
		
		return servicioRepository.save(servicio);
	}

	@PutMapping("/servicios/{id}")
	public ResponseEntity<Servicio> updateServicio(@PathVariable(value = "id") Long servicioId,
			@Valid @RequestBody Servicio servicioDetails) throws ResourceNotFoundException {
		Servicio servicio = servicioRepository.findById(servicioId)
				.orElseThrow(() -> new ResourceNotFoundException("Servicio not found for this id : " + servicioId));

		servicio.setFecServicio(servicioDetails.getFecServicio());
		
		final Servicio updatedServicio = servicioRepository.save(servicio);
		
		return ResponseEntity.ok(updatedServicio);
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

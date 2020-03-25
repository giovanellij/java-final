package com.java.alquiler.controllers;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java.alquiler.business.CreateVehiculo;
import com.java.alquiler.business.SearchVehiculoCriteria;
import com.java.alquiler.entities.Usuario;
import com.java.alquiler.entities.Vehiculo;
import com.java.alquiler.exceptions.RequestUnauthorizedException;
import com.java.alquiler.exceptions.ResourceNotFoundException;
import com.java.alquiler.repositories.UserRepository;
import com.java.alquiler.repositories.VehiculoRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1")
public class VehiculoController {
	@Autowired
	private VehiculoRepository vehiculoRepository;
	
	@Autowired
	private UserRepository usuarioRepository;

	@GetMapping("/vehiculos/{username}")
	public List<Vehiculo> getAllVehiculos(
			@PathVariable(value = "username") String username
			) throws ResourceNotFoundException, RequestUnauthorizedException {
		
		Usuario usuario = usuarioRepository.findByUserName(username)
				.orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado un usuario para ese nombre: " + username));
		
		if(!usuario.isAdministrador()) {			
			throw new RequestUnauthorizedException("El usuario no tiene permiso para realizar esta accion");			
		}
		
		return vehiculoRepository.findAll();
	}
	
	@GetMapping("/vehiculosDisponibles")
	public List<Vehiculo> getAllVehiculosDisponibles() {
		List<Vehiculo> vehiculosDisponibles = vehiculoRepository.findAllVehiculosDisponibles();
		return vehiculosDisponibles;
	}
	
	
	@GetMapping("/vehiculosAlquilados/{id}")
	public List<Vehiculo> getAllVehiculosAlquiladosByClienteId(@PathVariable(value = "id") Long clienteId) {
		return vehiculoRepository.findAllVehiculosAlquiladosByCliente(clienteId);
	}	
	
	@PostMapping("/vehiculosFiltered/{username}")
	public List<Vehiculo> getVehiculoByFilter(
			@PathVariable(value = "username") String username,
			@Valid @RequestBody SearchVehiculoCriteria criteria) throws ResourceNotFoundException, RequestUnauthorizedException {
		
		Usuario usuario = usuarioRepository.findByUserName(username)
				.orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado un usuario para ese nombre: " + username));
		
		if(!usuario.isAdministrador()) {			
			throw new RequestUnauthorizedException("El usuario no tiene permiso para realizar esta accion");			
		}
		
		List<Vehiculo> vehiculos = new ArrayList<Vehiculo>();		
		
		if(criteria.disponibles && criteria.pendientesDevolucion) {
			vehiculos = vehiculoRepository.findAllBySearchText(criteria.searchText);
		} else {
			if(criteria.disponibles) {
				vehiculos = vehiculoRepository.findDisponiblesBySearchText(criteria.searchText);
			} else {
				vehiculos = vehiculoRepository.findPendientesDevolucionBySearchText(criteria.searchText);
			}
		}
		
		return vehiculos;
	}
	
	@PostMapping("/vehiculos")
	public Vehiculo createVehiculo(
			@Valid @RequestBody CreateVehiculo vehiculo
			) throws RequestUnauthorizedException, ResourceNotFoundException {
		
		Usuario usuario = usuarioRepository.findByUserName(vehiculo.createdBy)
				.orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado un usuario para ese nombre: " + vehiculo.createdBy));
		
		if(!usuario.isAdministrador()) {			
			throw new RequestUnauthorizedException("El usuario no tiene permiso para realizar esta accion");			
		}
		
		Vehiculo newVehiculo = new Vehiculo(vehiculo);
		
		return vehiculoRepository.save(newVehiculo);
	}

	@PutMapping("/vehiculos/{id}")
	public ResponseEntity<Vehiculo> updateVehiculo(
			@PathVariable(value = "id") Long vehiculoId,
			@Valid @RequestBody CreateVehiculo vehiculoDetails
			) throws ResourceNotFoundException, RequestUnauthorizedException {
		
		Usuario usuario = usuarioRepository.findByUserName(vehiculoDetails.createdBy)
				.orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado un usuario para ese nombre: " + vehiculoDetails.createdBy));
		
		if(!usuario.isAdministrador()) {			
			throw new RequestUnauthorizedException("El usuario no tiene permiso para realizar esta accion");			
		}
		
		Vehiculo vehiculo = vehiculoRepository.findById(vehiculoId)
				.orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado un vehiculo para este id: " + vehiculoId));

		vehiculo.setPatente(vehiculoDetails.patente);
		vehiculo.setDescripcion(vehiculoDetails.descripcion);
		
		final Vehiculo updatedVehiculo = vehiculoRepository.save(vehiculo);
		
		return ResponseEntity.ok(updatedVehiculo);
	}
}

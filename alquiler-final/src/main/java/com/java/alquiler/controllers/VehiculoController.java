package com.java.alquiler.controllers;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.java.alquiler.business.SearchVehiculoCriteria;
import com.java.alquiler.entities.Vehiculo;
import com.java.alquiler.exceptions.ResourceNotFoundException;
import com.java.alquiler.repositories.VehiculoRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1")
public class VehiculoController {
	@Autowired
	private VehiculoRepository vehiculoRepository;

	@GetMapping("/vehiculos")
	public List<Vehiculo> getAllVehiculos() {
		return vehiculoRepository.findAll();
	}
	
	@GetMapping("/vehiculosDisponibles")
	public List<Vehiculo> getAllVehiculosDisponibles() {
		return vehiculoRepository.findAllVehiculosDisponibles();
	}

	@GetMapping("/vehiculo/{id}")
	public ResponseEntity<Vehiculo> getVehiculoById(@PathVariable(value = "id") Long vehiculoId)
			throws ResourceNotFoundException {
		Vehiculo vehiculo = vehiculoRepository.findById(vehiculoId)
				.orElseThrow(() -> new ResourceNotFoundException("Vehiculo not found for this id :: " + vehiculoId));
		return ResponseEntity.ok().body(vehiculo);
	}
	
	
	@PostMapping("/vehiculosFiltered")
	public List<Vehiculo> getVehiculoByFilter(@Valid @RequestBody SearchVehiculoCriteria criteria) throws ParseException {
		List<Vehiculo> vehiculos = new ArrayList<Vehiculo>();		
		
		if(criteria.disponibles && criteria.pendientesDevolucion) {
			vehiculos = vehiculoRepository.findAllBetweenDatesAndSearchText(criteria.searchText);
		} else {
			if(criteria.disponibles) {
//				vehiculos = vehiculoRepository.findDisponiblesBetweenDatesAndSearchText(criteria.from.trim(), criteria.to.trim(), criteria.searchText);
			} else {
//				vehiculos = vehiculoRepository.findPendientesDevolucionBetweenDatesAndSearchText(criteria.from.trim(), criteria.to.trim(), criteria.searchText);
			}
		}
		return vehiculos;
	}
	

	@PostMapping("/vehiculos")
	public Vehiculo createEmployee(@Valid @RequestBody Vehiculo vehiculo) {
		return vehiculoRepository.save(vehiculo);
	}

	@PutMapping("/vehiculos/{id}")
	public ResponseEntity<Vehiculo> updateVehiculo(@PathVariable(value = "id") Long vehiculoId,
			@Valid @RequestBody Vehiculo vehiculoDetails) throws ResourceNotFoundException {
		Vehiculo vehiculo = vehiculoRepository.findById(vehiculoId)
				.orElseThrow(() -> new ResourceNotFoundException("Vehiculo not found for this id : " + vehiculoId));

		vehiculo.setPatente(vehiculoDetails.getPatente());
		vehiculo.setDescripcion(vehiculoDetails.getDescripcion());
		final Vehiculo updatedVehiculo = vehiculoRepository.save(vehiculo);
		return ResponseEntity.ok(updatedVehiculo);
	}

	@DeleteMapping("/vehiculos/{id}")
	public Map<String, Boolean> deleteVehiculo(@PathVariable(value = "id") Long vehiculoId)
			throws ResourceNotFoundException {
		Vehiculo vehiculo = vehiculoRepository.findById(vehiculoId)
				.orElseThrow(() -> new ResourceNotFoundException("Vehiculo not found for this id : " + vehiculoId));

		vehiculo.setAlquilado(!vehiculo.getAlquilado());

		vehiculoRepository.save(vehiculo);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}

package com.java.alquiler.controllers;

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

import com.java.alquiler.business.CreateCliente;
import com.java.alquiler.entities.Cliente;
import com.java.alquiler.entities.Usuario;
import com.java.alquiler.exceptions.RequestUnauthorizedException;
import com.java.alquiler.exceptions.ResourceNotFoundException;
import com.java.alquiler.repositories.ClienteRepository;
import com.java.alquiler.repositories.UserRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1")
public class ClienteController {
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private UserRepository usuarioRepository;

	@GetMapping("/clientes/{username}")
	public List<Cliente> getAllClientes(@PathVariable(value = "username") String username) throws ResourceNotFoundException, RequestUnauthorizedException {
		
		Usuario usuario = usuarioRepository.findByUserName(username)
				.orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado un usuario para ese nombre: " + username));
		
		if(!usuario.isAdministrador()) {			
			throw new RequestUnauthorizedException("El usuario no tiene permiso para realizar esta accion");			
		}
		
		return clienteRepository.findAll();
	}
	@GetMapping("/clientesActivos/{username}")
	public List<Cliente> getAllClientesActivos(@PathVariable(value = "username") String username) throws ResourceNotFoundException, RequestUnauthorizedException {
		
		Usuario usuario = usuarioRepository.findByUserName(username)
				.orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado un usuario para ese nombre: " + username));
		
		if(!usuario.isAdministrador()) {			
			throw new RequestUnauthorizedException("El usuario no tiene permiso para realizar esta accion");			
		}
		
		return clienteRepository.findAllClientesActivos();
	}
	
	@PostMapping("/clientes")
	public Cliente createCliente(@Valid @RequestBody CreateCliente cliente) throws ResourceNotFoundException, RequestUnauthorizedException {
		
		Usuario usuario = usuarioRepository.findByUserName(cliente.createdBy)
				.orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado un usuario para ese nombre: " + cliente.createdBy));
		
		if(!usuario.isAdministrador()) {			
			throw new RequestUnauthorizedException("El usuario no tiene permiso para realizar esta accion");			
		}
		
		Cliente newCliente = new Cliente(cliente);
		
		return clienteRepository.save(newCliente);
	}

	@PutMapping("/clientes/{id}")
	public ResponseEntity<Cliente> updateCliente(@PathVariable(value = "id") Long clienteId,
			@Valid @RequestBody CreateCliente clienteDetails) throws ResourceNotFoundException, RequestUnauthorizedException {
		
		Usuario usuario = usuarioRepository.findByUserName(clienteDetails.createdBy)
				.orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado un usuario para ese nombre: " + clienteDetails.createdBy));
		
		if(!usuario.isAdministrador()) {			
			throw new RequestUnauthorizedException("El usuario no tiene permiso para realizar esta accion");			
		}
		
		Cliente cliente = clienteRepository.findById(clienteId)
				.orElseThrow(() -> new ResourceNotFoundException("Cliente not found for this id : " + clienteId));

		cliente.setNroDocumento(clienteDetails.nroDocumento);
		cliente.setNombre(clienteDetails.nombre);
		cliente.setApellido(clienteDetails.apellido);
		cliente.setEmail(clienteDetails.email);
		cliente.setDireccion(clienteDetails.direccion);
		cliente.setTelefono(clienteDetails.telefono);
		
		final Cliente updatedCliente = clienteRepository.save(cliente);
		
		return ResponseEntity.ok(updatedCliente);
	}

	@DeleteMapping("/clientes/{id}/{username}")
	public Map<String, Boolean> deleteCliente(
			@PathVariable(value = "id") Long clienteId,
			@PathVariable(value = "username") String username
			) throws ResourceNotFoundException, RequestUnauthorizedException {
		
		Usuario usuario = usuarioRepository.findByUserName(username)
				.orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado un usuario para ese nombre: " + username));
		
		if(!usuario.isAdministrador()) {			
			throw new RequestUnauthorizedException("El usuario no tiene permiso para realizar esta accion");			
		}
		
		Cliente cliente = clienteRepository.findById(clienteId)
				.orElseThrow(() -> new ResourceNotFoundException("Cliente not found for this id : " + clienteId));

		cliente.setActivo(!cliente.getActivo());

		clienteRepository.save(cliente);
		
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}

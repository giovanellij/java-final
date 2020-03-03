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

import com.java.alquiler.entities.Cliente;
import com.java.alquiler.exceptions.ResourceNotFoundException;
import com.java.alquiler.repositories.ClienteRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1")
public class ClienteController {
	@Autowired
	private ClienteRepository clienteRepository;

	@GetMapping("/clientes")
	public List<Cliente> getAllClientes() {
		return clienteRepository.findAll();
	}
	@GetMapping("/cliente/{id}")
	public ResponseEntity<Cliente> getClienteById(@PathVariable(value = "id") Long clienteId)
			throws ResourceNotFoundException {
		Cliente cliente = clienteRepository.findById(clienteId)
				.orElseThrow(() -> new ResourceNotFoundException("Cliente not found for this id :: " + clienteId));
		return ResponseEntity.ok().body(cliente);
	}

	@PostMapping("/clientes")
	public Cliente createCliente(@Valid @RequestBody Cliente cliente) {
		return clienteRepository.save(cliente);
	}

	@PutMapping("/clientes/{id}")
	public ResponseEntity<Cliente> updateCliente(@PathVariable(value = "id") Long clienteId,
			@Valid @RequestBody Cliente clienteDetails) throws ResourceNotFoundException {
		Cliente cliente = clienteRepository.findById(clienteId)
				.orElseThrow(() -> new ResourceNotFoundException("Cliente not found for this id : " + clienteId));

		cliente.setNroDocumento(clienteDetails.getNroDocumento());
		cliente.setNombre(clienteDetails.getNombre());
		cliente.setApellido(clienteDetails.getApellido());
		cliente.setEmail(clienteDetails.getEmail());
		cliente.setDireccion(clienteDetails.getDireccion());
		cliente.setTelefono(clienteDetails.getTelefono());
		
		final Cliente updatedCliente = clienteRepository.save(cliente);
		
		return ResponseEntity.ok(updatedCliente);
	}

	@DeleteMapping("/clientes/{id}")
	public Map<String, Boolean> deleteCliente(@PathVariable(value = "id") Long clienteId)
			throws ResourceNotFoundException {
		Cliente cliente = clienteRepository.findById(clienteId)
				.orElseThrow(() -> new ResourceNotFoundException("Cliente not found for this id : " + clienteId));

		cliente.setActivo(!cliente.getActivo());

		clienteRepository.save(cliente);
		
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}

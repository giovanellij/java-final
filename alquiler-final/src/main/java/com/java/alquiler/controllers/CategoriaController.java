package com.java.alquiler.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java.alquiler.entities.Categoria;
import com.java.alquiler.exceptions.ResourceNotFoundException;
import com.java.alquiler.repositories.CategoriaRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1")
public class CategoriaController {
	
	@Autowired
	private CategoriaRepository categoriaRepository;

	@GetMapping("/categorias")
	public List<Categoria> getAllCategorias() throws ResourceNotFoundException {
		List<Categoria> categorias = categoriaRepository.findAll();
		return categorias;
	}

}

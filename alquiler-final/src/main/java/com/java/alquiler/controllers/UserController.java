package com.java.alquiler.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java.alquiler.business.LoginUser;
import com.java.alquiler.entities.Usuario;
import com.java.alquiler.exceptions.ResourceNotFoundException;
import com.java.alquiler.repositories.UserRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1")
public class UserController {
	
	
	@Autowired
	private UserRepository userRepository;
	
	@PostMapping("/usuarios")
	public ResponseEntity<Usuario> login(@Valid @RequestBody LoginUser user) throws ResourceNotFoundException {
		Usuario loguedUser = null;
		
		if(user.getUserName().toLowerCase().equals("admin")) {
			loguedUser = userRepository.findByUserNameAndPassword(user.userName, user.password)
					.orElseThrow(() -> new ResourceNotFoundException("Usuario o contraseña inválidos"));
		} else {
			loguedUser = userRepository.login(user.userName, user.password)
					.orElseThrow(() -> new ResourceNotFoundException("Usuario o contraseña inválidos"));
		}
		
		
		return ResponseEntity.ok().body(loguedUser);
	}
}

package com.java.alquiler.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.java.alquiler.entities.Usuario;

@Repository
public interface UserRepository  extends JpaRepository<Usuario, Long>{
	@Query("select u from Usuario u where u.userName like %:username% AND u.password like %:password%")
	Optional<Usuario> findByUserNameAndPassword(
    		@Param("username")String username, 
    		@Param("password")String password);
	
	@Query("select u from Usuario u where u.userName like %:username% AND u.password like %:password% AND u.cliente.activo = true")
	Optional<Usuario> login(
    		@Param("username")String username, 
    		@Param("password")String password);
	
	@Query("select u from Usuario u where u.userName like %:username%")
	Optional<Usuario> findByUserName(
    		@Param("username") String username);
}

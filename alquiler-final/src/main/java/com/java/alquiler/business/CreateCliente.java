package com.java.alquiler.business;

import java.util.ArrayList;
import java.util.List;

import com.java.alquiler.entities.Servicio;

public class CreateCliente {

	public long id;
	public String nroDocumento;
	public Boolean activo;
	public String apellido;
	public String nombre;
	public String email;
	public String telefono;
	public String direccion;
	public List<Servicio> servicios = new ArrayList<>();
	public String createdBy;
	
}

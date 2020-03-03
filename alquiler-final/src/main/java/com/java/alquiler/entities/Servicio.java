package com.java.alquiler.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "Servicio")
@Table(name = "servicios")
public class Servicio {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Column(name = "fecServicio", nullable = false)
	private Date fecServicio;
	@Column(name = "fecCancelacion", nullable = true)
	private Date fecCancelacion;
	@Column(name = "nroReserva", nullable = false)
	private String nroReserva;
	
	@Column(name = "vehiculoId", nullable = false)
	private long vehiculoId;
	public long getVehiculoId() {
		return vehiculoId;
	}


	public void setVehiculoId(long vehiculoId) {
		this.vehiculoId = vehiculoId;
	}


	public long getClienteId() {
		return clienteId;
	}


	public void setClienteId(long clienteId) {
		this.clienteId = clienteId;
	}

	@Column(name = "clienteId", nullable = false)
	private long clienteId;
	
	public long getId() {
		return id;
	}
	

	public void setId(long id) {
		this.id = id;
	}

	public Date getFecServicio() {
		return fecServicio;
	}

	public void setFecServicio(Date fecServicio) {
		this.fecServicio = fecServicio;
	}

	public Date getFecCancelacion() {
		return fecCancelacion;
	}

	public void setFecCancelacion(Date fecCancelacion) {
		this.fecCancelacion = fecCancelacion;
	}

	public String getNroReserva() {
		return nroReserva;
	}

	public void setNroReserva(String nroReserva) {
		this.nroReserva = nroReserva;
	}

	public Servicio() {}
}

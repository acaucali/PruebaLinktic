package com.producto.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;

@Entity
public class Producto implements Serializable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Size(max=200)
	@Column(nullable=false)
	private String nombre;
	
	
	@Size(max=20)
	@Column(nullable=true)
	private Double precio;
	
	@Size(max=500)
	@Column(nullable=true)
	private String descripcion;

	
	public Producto(Long id, String nombre, Double pre, String des) {
		this.id = id;
		this.nombre = nombre;
		this.precio = pre;
		this.descripcion = des;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}	

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	private static final long serialVersionUID = 1L;
	
}

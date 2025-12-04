package com.inventario.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventario.entity.Inventario;


public interface InventarioDao extends JpaRepository<Inventario, Long>{
	
	Inventario findByProductoId(Long productoId);

}

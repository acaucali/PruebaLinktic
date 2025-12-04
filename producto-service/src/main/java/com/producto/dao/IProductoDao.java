package com.producto.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.producto.entity.Producto;

public interface IProductoDao extends JpaRepository<Producto, Long>{

}

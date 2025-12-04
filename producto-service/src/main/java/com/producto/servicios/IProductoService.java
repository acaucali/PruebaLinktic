package com.producto.servicios;

import java.util.List;

import com.producto.entity.Producto;

public interface IProductoService {
	
	public List<Producto> findAll();
	
	public Producto findById(Long id);

	public Producto save(Producto producto);
	
	public void delete(Long id);

}

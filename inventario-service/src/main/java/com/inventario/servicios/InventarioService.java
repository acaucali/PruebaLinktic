package com.inventario.servicios;

import java.util.List;
import com.inventario.entity.Inventario;

public interface InventarioService {
	
	public List<Inventario> findAll();
	
	public Inventario findById(Long id);
	
	public Inventario findByProductoId(Long productoId);

	public Inventario save(Inventario inventario);
	
	public void delete(Long id);

}

package com.inventario.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventario.entity.Inventario;
import com.inventario.servicios.InventarioService;



@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/api/compras")
public class CompraRestController {
	
	@Autowired
	private InventarioService inventarioService;
	
	
	@PostMapping("/compra/{id}/{cantidad}")
	public ResponseEntity<?> realizarCompra(@PathVariable Long id, @PathVariable Integer cantidad) {
		
		Inventario producto= null;
		Map<String, Object> response = new HashMap<>();
				
		
		try { 
		
			producto= inventarioService.findByProductoId(id);
			
			if(producto != null) {
								
				if(producto.getCantidad() == null || producto.getCantidad() == 0 || producto.getCantidad() < cantidad) {
					response.put("mensaje", "No hay suficientes unidades para comprar en el inventario, Unidades disponibles: ".concat(producto.getCantidad().toString())); 	
					return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
				}else {
					
					Integer unidades = producto.getCantidad();
					Integer disponible = unidades - cantidad;
					
					
					producto.setCantidad(disponible);
					inventarioService.save(producto);
				}
				
			}else {
				response.put("mensaje", "El producto ID: ".concat(id.toString().concat(" no existe en la base de datos!"))); 	
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
			
		
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "La compra ha sido registrada con éxito! /n Detalles de la compra: /n Producto: "+"Cantidad: "+cantidad);
		return new ResponseEntity<Map<String, Object>> (response,HttpStatus.CREATED);
	}

}

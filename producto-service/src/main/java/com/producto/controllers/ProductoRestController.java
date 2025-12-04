package com.producto.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.producto.entity.Producto;
import com.producto.servicios.IProductoService;

import jakarta.validation.Valid;

@CrossOrigin(origins= {"*" })
@RestController
@RequestMapping("/api/productos")
public class ProductoRestController {

	@Autowired
	private IProductoService productoService;
	
	private final Logger log = LoggerFactory.getLogger(ProductoRestController.class);
	
	@GetMapping("/producto")
	public List<Producto> index (){
		return productoService.findAll ();
	}

	@GetMapping("/producto/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		
		Producto producto=null;
		Map<String, Object> response = new HashMap<>();
		
		try { 
			producto= productoService.findById(id);
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
		if(producto == null) {
		  response.put("mensaje", "El Producto ID: ".concat(id.toString().concat(" no existe en la base de datos!"))); 	
		  return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Producto>(producto, HttpStatus.OK); 		
	}
	
	@PostMapping("/producto")
	public ResponseEntity<?> create(@Valid @RequestBody Producto Producto, BindingResult result) {
		
		Producto productoNew= null;
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			
			List<String> errors= result.getFieldErrors().stream().map(err ->
				"Campo: "+err.getField()+" "+err.getDefaultMessage()
			).collect(Collectors.toList());
			
			response.put("errors", errors);
		    return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		try { 
		
			productoNew= productoService.save(Producto);
		
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El producto ha sido creado con éxito!");
		response.put("producto", productoNew);
		return new ResponseEntity<Map<String, Object>> (response,HttpStatus.CREATED);
	}

	@PutMapping("/producto/{id}")
	public ResponseEntity<?>  update(@Valid @RequestBody Producto producto, BindingResult result, @PathVariable Long id) {
		Producto productoActual= productoService.findById(id);
		Producto productoUpdated = null;
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			
			List<String> errors= result.getFieldErrors().stream().map(err ->
				"Campo: "+err.getField()+" "+err.getDefaultMessage()
			).collect(Collectors.toList());
			
			response.put("errors", errors);
		    return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(productoActual == null) {
			  response.put("mensaje", "Error, no se pudo editar, el producto ID: ".concat(id.toString().concat(" no existe en la base de datos!"))); 	
			  return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try{
			productoActual.setDescripcion(producto.getDescripcion());
			productoActual.setNombre(producto.getDescripcion());
			productoActual.setPrecio(producto.getPrecio());
			
			productoUpdated=productoService.save(productoActual);
		
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al actualizar el producto en la base de datos!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El producto ha sido actualizado con éxito!");
		response.put("producto", productoUpdated);
		return new ResponseEntity<Map<String, Object>> (response,HttpStatus.CREATED);
	}
	
	@DeleteMapping("/producto/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		
		Map<String, Object> response = new HashMap<>();
		
		try{
			
			productoService.delete(id);
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al eliminar el producto en la base de datos!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El producto ha sido eliminado con éxito!");
		return new ResponseEntity<Map<String, Object>> (response,HttpStatus.OK);
	}

	
}

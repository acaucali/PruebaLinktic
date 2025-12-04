package com.inventario.controllers;

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

import com.inventario.entity.Inventario;
import com.inventario.servicios.InventarioService;

import jakarta.validation.Valid;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/api/servicios")
public class InventarioRestController {
	
	@Autowired
	private InventarioService inventarioService;
	
	private final Logger log = LoggerFactory.getLogger(InventarioRestController.class);
	
	@GetMapping("/inventario")
	public List<Inventario> index (){
		return inventarioService.findAll ();
	}

	@GetMapping("/inventario/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		
		Inventario inventario=null;
		Map<String, Object> response = new HashMap<>();
		
		try { 
			inventario= inventarioService.findById(id);
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
		if(inventario == null) {
		  response.put("mensaje", "El Inventario ID: ".concat(id.toString().concat(" no existe en la base de datos!"))); 	
		  return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Inventario>(inventario, HttpStatus.OK); 		
	}
	
	@GetMapping("/inventario/consulta/{id}")
	public ResponseEntity<?> consultaCantidadProducto(@PathVariable Long id) {
		
Inventario inventarioActual= inventarioService.findByProductoId(id);
		
		Map<String, Object> response = new HashMap<>();
		Integer cantidad = 0;
		
		if(inventarioActual == null) {
			  response.put("mensaje", "Error, no se pudo actualizar el inventario, el producto ID: ".concat(id.toString().concat(" no existe en la base de datos!"))); 	
			  return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try{
			
			cantidad =inventarioActual.getCantidad();
			
		
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El producto Id: ".concat(id.toString().concat("tiene: ").concat(cantidad.toString()).concat("disponible")));
		return new ResponseEntity<Map<String, Object>> (response,HttpStatus.OK);		
	}
	
	
	@PostMapping("/inventario")
	public ResponseEntity<?> create(@Valid @RequestBody Inventario inventario, BindingResult result) {
		
		Inventario inventarioNew= null;
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			
			List<String> errors= result.getFieldErrors().stream().map(err ->
				"Campo: "+err.getField()+" "+err.getDefaultMessage()
			).collect(Collectors.toList());
			
			response.put("errors", errors);
		    return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		try { 
		
			inventarioNew= inventarioService.save(inventario);
		
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El inventario ha sido registrado con éxito!");
		response.put("Inventario", inventarioNew);
		return new ResponseEntity<Map<String, Object>> (response,HttpStatus.CREATED);
	}

	
	@PutMapping("/inventario/producto/{id}/{can}") //actualiza la cantidad disponible de un producto
	public ResponseEntity<?>  update(@PathVariable Long id, @PathVariable Integer can) {
		
		Inventario inventarioActual= inventarioService.findByProductoId(id);
		
		Map<String, Object> response = new HashMap<>();
		
		
		if(inventarioActual == null) {
			  response.put("mensaje", "Error, no se pudo actualizar el inventario, el producto ID: ".concat(id.toString().concat(" no existe en la base de datos!"))); 	
			  return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		try{
			
			inventarioActual.setCantidad(can);
			inventarioService.save(inventarioActual);
		
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al actualizar el Inventario en la base de datos!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El Inventario ha sido actualizado con éxito!");
		return new ResponseEntity<Map<String, Object>> (response,HttpStatus.CREATED);
	}
	
	@DeleteMapping("/inventario/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		
		Map<String, Object> response = new HashMap<>();
		
		try{
			
			inventarioService.delete(id);
		}catch(DataAccessException e) {
			response.put("mensaje", "Error al eliminar el Inventario en la base de datos!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El Inventario ha sido eliminado con éxito!");
		return new ResponseEntity<Map<String, Object>> (response,HttpStatus.OK);
	}

}

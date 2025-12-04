package com.producto.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;

import org.mockito.junit.jupiter.MockitoExtension;

import com.producto.entity.Producto;
import com.producto.servicios.ProductoServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {
	

	    @InjectMocks
	    private ProductoServiceImpl service;

	    @Test
	    void crearProducto() {
	        Producto p = new Producto(null, "Laptop", 2000.0, "Laptop acer");

	        Producto result = service.save(p);

	        assertNotNull(result);
	        assertEquals("Laptop", result.getNombre());
	    }

}

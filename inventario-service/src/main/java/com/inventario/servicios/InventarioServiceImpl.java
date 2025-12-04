package com.inventario.servicios;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.inventario.dao.InventarioDao;
import com.inventario.entity.Inventario;
import com.inventario.entity.Producto;

import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.netty.http.client.HttpClient;


@Service
public class InventarioServiceImpl implements InventarioService{
	
	private final WebClient webClient;

    public InventarioServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:9090/api/producto/servicios")// Reemplaza con la URL de tu servicio
        		.defaultHeader("Authorization", "prueba-linktic-service")
        		.clientConnector(
        		        new ReactorClientHttpConnector(
        		            HttpClient.create()
        		                .responseTimeout(Duration.ofSeconds(5))
        		                .doOnConnected(conn -> conn.addHandler(new ReadTimeoutHandler(5))
        		                    .addHandler(new WriteTimeoutHandler(5)))
        		        )
        		)
                .build();
    }
    
    
    
    public Producto obtenerProductoPorId(String id) {
        return this.webClient.get()
                .uri("/producto/{id}", id)
                .retrieve()
                .bodyToMono(Producto.class)
                .block(); 
    }
	
	@Autowired
	private InventarioDao inventarioDao;

	@Override
	public List<Inventario> findAll() {
		
		return (List<Inventario>) inventarioDao.findAll();
	}

	@Override
	public Inventario findById(Long id) {
		
		return inventarioDao.findById(id).orElse(null);
	}

	@Override
	public Inventario save(Inventario inventario) {
		
		return inventarioDao.save(inventario);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		
		inventarioDao.deleteById(id);
	}

	@Override
	public Inventario findByProductoId(Long productoId) {
		
		return inventarioDao.findByProductoId(productoId);
	}

}

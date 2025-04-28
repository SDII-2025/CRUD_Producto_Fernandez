package com.example.crud_cosmodb.services;

import com.example.crud_cosmodb.messaging.AzureServiceBusSender;
import com.example.crud_cosmodb.entities.Producto;
import com.example.crud_cosmodb.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private AzureServiceBusSender serviceBusSender;

    public List<Producto> getProductos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> getProductoByCodigoBarras(String codigoBarras) {
        return productoRepository.findByCodigoBarras(codigoBarras);
    }

    public Producto crearProducto(Producto producto) {
        boolean existe = productoRepository.existsById(producto.getCodigoBarras());

        if (existe) {
            throw new IllegalArgumentException("Ya existe un producto con ese código de barras");
        }

        Producto productoGuardado = productoRepository.save(producto);

        try {
            ObjectMapper mapper = new ObjectMapper();
            String mensaje = mapper.writeValueAsString(productoGuardado);

            serviceBusSender.enviarMensaje(mensaje);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear mensaje", e);
        }

        return productoGuardado;
    }

    public boolean eliminarProducto(String codigoBarras) {
        if (productoRepository.existsById(codigoBarras)) {
            productoRepository.deleteById(codigoBarras);
            return true;
        } else {
            return false;
        }
    }
}

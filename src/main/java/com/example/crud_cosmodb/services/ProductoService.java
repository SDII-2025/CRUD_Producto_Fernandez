package com.example.crud_cosmodb.services;

import com.example.crud_cosmodb.messaging.AzureServiceBusSender;
import com.example.crud_cosmodb.entities.Producto;
import com.example.crud_cosmodb.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
            throw new IllegalArgumentException("Ya existe un producto con ese codigo de barras");
        }

        Producto productoGuardado = productoRepository.save(producto);

        String mensaje = String.format("Producto creado: %s - %s - %d",
                productoGuardado.getCodigoBarras(),
                productoGuardado.getNombre(),
                productoGuardado.getPrecio());
        serviceBusSender.enviarMensaje(mensaje);

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

package com.example.crud_cosmodb.services;

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
        return productoRepository.save(producto);
    }

    public boolean eliminarProducto(String codigoBarras) {
        if (productoRepository.existsById(codigoBarras)) {
            productoRepository.deleteById(codigoBarras);
            return true;
        } else {
            return false;
        }
    }

    public Producto actualizarProducto(String codigoBarras, Producto producto) {
        Producto productoExistente = productoRepository.findByCodigoBarras(codigoBarras)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        if (!productoExistente.getCodigoBarras().equals(producto.getCodigoBarras())) {
            productoExistente.setCodigoBarras(producto.getCodigoBarras());
        }

        productoExistente.setNombre(producto.getNombre());
        productoExistente.setPrecio(producto.getPrecio());

        return productoRepository.save(productoExistente);
    }
}
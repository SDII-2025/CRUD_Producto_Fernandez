package com.example.crud_cosmodb.controllers;

import com.example.crud_cosmodb.entities.Producto;
import com.example.crud_cosmodb.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<Producto>> obtenerProductos() {
        List<Producto> productos = productoService.getProductos();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{codigoBarras}")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable String codigoBarras) {

        Optional<Producto> producto = productoService.getProductoByCodigoBarras(codigoBarras);

        return producto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<?> crearProducto(@RequestBody Producto producto) {
        try {
            Producto productoGuardado = productoService.crearProducto(producto);
            return ResponseEntity.status(HttpStatus.CREATED).body(productoGuardado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{codigoBarras}")
    public ResponseEntity<?> eliminarProducto(@PathVariable String codigoBarras) {
        boolean eliminado = productoService.eliminarProducto(codigoBarras);

        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontro un producto con ese codigo de barras");
        }
    }

    @PutMapping("/{codigoBarras}")
    public ResponseEntity<?> actualizarProducto(@PathVariable String codigoBarras, @RequestBody Producto producto) {
        Optional<Producto> productoExistente = productoService.getProductoByCodigoBarras(codigoBarras);

        if (productoExistente.isPresent()) {
            if (!producto.getCodigoBarras().equals(codigoBarras)) {
                Optional<Producto> productoConNuevoCodigo = productoService.getProductoByCodigoBarras(producto.getCodigoBarras());
                if (productoConNuevoCodigo.isPresent()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Ya existe un producto con ese codigo de barras");
                }
            }

            productoService.eliminarProducto(codigoBarras);

            Producto nuevoProducto = new Producto(producto.getCodigoBarras(), producto.getNombre(), producto.getPrecio());
            Producto productoCreado = productoService.crearProducto(nuevoProducto);

            return ResponseEntity.ok(productoCreado);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Producto con ese codigo de barras no encontrado");
        }
    }
}

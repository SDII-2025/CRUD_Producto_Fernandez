package com.example.crud_cosmodb.controllers;

import com.example.crud_cosmodb.entities.Producto;
import com.example.crud_cosmodb.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/productos")
@Tag(name = "Productos", description = "Operaciones relacionadas con productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Operation(summary = "Obtener todos los productos", description = "Devuelve una lista de todos los productos")
    @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<Producto>> obtenerProductos() {
        List<Producto> productos = productoService.getProductos();
        return ResponseEntity.ok(productos);
    }

    @Operation(summary = "Obtener un producto por código de barras",
            description = "Devuelve el producto correspondiente al código de barras proporcionado")
    @ApiResponse(responseCode = "200", description = "Producto encontrado")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    @GetMapping("/{codigoBarras}")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable String codigoBarras) {
        Optional<Producto> producto = productoService.getProductoByCodigoBarras(codigoBarras);
        return producto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Crear un nuevo producto", description = "Agrega un nuevo producto a la base de datos.")
    @ApiResponse(responseCode = "201", description = "Producto creado exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos para crear el producto")
    @PostMapping
    public ResponseEntity<?> crearProducto(@RequestBody Producto producto) {
        try {
            Producto productoGuardado = productoService.crearProducto(producto);
            return ResponseEntity.status(HttpStatus.CREATED).body(productoGuardado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Eliminar un producto", description = "Elimina un producto por su código de barras")
    @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    @DeleteMapping("/{codigoBarras}")
    public ResponseEntity<?> eliminarProducto(@PathVariable String codigoBarras) {
        boolean eliminado = productoService.eliminarProducto(codigoBarras);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró un producto con ese código de barras");
        }
    }

    @Operation(summary = "Actualizar un producto", description = "Actualiza los datos de un producto existente")
    @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente")
    @ApiResponse(responseCode = "400", description = "Error de validación o código de barras en uso")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    @PutMapping("/{codigoBarras}")
    public ResponseEntity<?> actualizarProducto(@PathVariable String codigoBarras, @RequestBody Producto producto) {
        Optional<Producto> productoExistente = productoService.getProductoByCodigoBarras(codigoBarras);
        if (productoExistente.isPresent()) {
            if (!producto.getCodigoBarras().equals(codigoBarras)) {
                Optional<Producto> productoConNuevoCodigo = productoService.getProductoByCodigoBarras(producto.getCodigoBarras());
                if (productoConNuevoCodigo.isPresent()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Ya existe un producto con ese código de barras");
                }
            }
            productoService.eliminarProducto(codigoBarras);
            Producto nuevoProducto = new Producto(producto.getCodigoBarras(), producto.getNombre(), producto.getPrecio());
            Producto productoCreado = productoService.crearProducto(nuevoProducto);
            return ResponseEntity.ok(productoCreado);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Producto con ese código de barras no encontrado");
        }
    }
}

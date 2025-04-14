package com.example.crud_cosmodb.repositories;

import com.example.crud_cosmodb.entities.Producto;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends MongoRepository<Producto, String> {

    @Override
    List<Producto> findAll(Sort sort);

    Optional<Producto> findByCodigoBarras(String codigoBarras);

    //Optional<Producto> updateProducto(Producto producto);

}
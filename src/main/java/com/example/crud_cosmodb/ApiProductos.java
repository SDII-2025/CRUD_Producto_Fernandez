package com.example.crud_cosmodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.example.crud_cosmodb.entities.Producto;
import com.example.crud_cosmodb.repositories.ProductoRepository;


import java.util.Optional;
import java.util.Scanner;

@SpringBootApplication
public class ApiProductos {

	public static void main(String[] args) {
		SpringApplication.run(ApiProductos.class, args);
	}

}


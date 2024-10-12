package com.nequi.models.repository;

import com.nequi.models.documents.Producto;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;


public interface ProductoRepository extends ReactiveMongoRepository<Producto, String> {
}

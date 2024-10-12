package com.nequi.models.repository;

import com.nequi.models.documents.Sucursal;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface SucursalRepository extends ReactiveMongoRepository<Sucursal,String> {
}

package com.nequi.models.repository;

import com.nequi.models.documents.Franquicia;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface FranquiciaRespository extends ReactiveMongoRepository<Franquicia,String> {
}

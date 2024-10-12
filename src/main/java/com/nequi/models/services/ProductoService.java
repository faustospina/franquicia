package com.nequi.models.services;

import com.nequi.models.dto.ProductoDTO;
import reactor.core.publisher.Mono;

public interface ProductoService {

    Mono<ProductoDTO> create(ProductoDTO productoDTO);

    Mono<ProductoDTO> updateNameProducto(String id,String name);

    Mono<ProductoDTO> updateStockProducto(String id,Integer stock);


}

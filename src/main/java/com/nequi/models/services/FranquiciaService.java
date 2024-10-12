package com.nequi.models.services;


import com.nequi.models.dto.FranquiciaDTO;
import com.nequi.models.dto.ProductoStockDTO;
import com.nequi.models.dto.SucursalDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface FranquiciaService {
    Mono<FranquiciaDTO> create(String nombre);

    Mono<FranquiciaDTO> addSucursalToFranquicia(String franquiciaId, SucursalDTO sucursalDTO);

    Mono<List<ProductoStockDTO>> getProductoConMayorStockPorSucursal(String id);

    Mono<FranquiciaDTO> updateNameFranquicia(String id,String name);
}

package com.nequi.models.services;


import com.nequi.models.dto.FranquiciaDTO;
import com.nequi.models.dto.ProductoStockDTO;
import com.nequi.models.dto.SucursalDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface FranquiciaService {
    Mono<FranquiciaDTO> create(FranquiciaDTO franquiciaDTO);

    Mono<FranquiciaDTO> addSucursalToFranquicia(String franquiciaId, SucursalDTO sucursalDTO);

    Mono<List<ProductoStockDTO>> getProductoConMayorStockPorSucursal(String franquiciaId);

    Mono<FranquiciaDTO> updateNameFranquicia(String id,String name);
}

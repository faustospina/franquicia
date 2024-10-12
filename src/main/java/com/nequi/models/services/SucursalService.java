package com.nequi.models.services;

import com.nequi.models.dto.ProductoDTO;
import com.nequi.models.dto.SucursalDTO;
import reactor.core.publisher.Mono;

public interface SucursalService {

    Mono<SucursalDTO> create(String nombre);

    Mono<SucursalDTO> createSucursalIntranet(SucursalDTO sucursalDTO);

    Mono<SucursalDTO> addNewProductToSucursal(String id, ProductoDTO productoDTO);
    Mono<SucursalDTO> removeProductFromSucursal(String id, String productoId);

    Mono<SucursalDTO> updateNameSucursal(String id,String name);

}

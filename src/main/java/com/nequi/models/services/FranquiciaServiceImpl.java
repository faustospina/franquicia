package com.nequi.models.services;

import com.nequi.exception.BusinessException;
import com.nequi.exception.ExceptionType;
import com.nequi.models.documents.Franquicia;
import com.nequi.models.documents.Producto;
import com.nequi.models.documents.Sucursal;
import com.nequi.models.dto.FranquiciaDTO;
import com.nequi.models.dto.ProductoStockDTO;
import com.nequi.models.dto.SucursalDTO;
import com.nequi.models.mapper.FranquiciaMapper;
import com.nequi.models.mapper.ProductoMapper;
import com.nequi.models.mapper.SucursalMapper;
import com.nequi.models.repository.FranquiciaRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class FranquiciaServiceImpl implements FranquiciaService {

    public static final String NOT_FOUND_FRANQUICIA = "Not found franquicia";
    @Autowired
    private FranquiciaRespository repository;

    @Autowired
    private FranquiciaMapper mapper;

    @Autowired
    private SucursalMapper sucursalMapper;

    @Autowired
    private ProductoMapper productoMapper;

    @Autowired
    private SucursalService sucursalService;

    @Override
    public Mono<FranquiciaDTO> create(FranquiciaDTO franquiciaDTO) {
        Franquicia franquicia = mapper.toEntity(franquiciaDTO);

        return repository.save(franquicia)
                .map(savedFranquicia -> mapper.toDTO(savedFranquicia));
    }

    @Override
    public Mono<FranquiciaDTO> addSucursalToFranquicia(String franquiciaId, SucursalDTO sucursalDTO) {
        return repository.findById(franquiciaId)
                .flatMap(franquicia ->
                        sucursalService.create(sucursalDTO)
                                .flatMap(nuevoSucursal -> {
                                    Sucursal sucursal = sucursalMapper.toEntity(nuevoSucursal);

                                    if (sucursal.getProductos() == null) {
                                        sucursal.setProductos(new ArrayList<>());
                                    }

                                    franquicia.getSucursales().add(sucursal);

                                    return repository.save(franquicia);
                                })
                )
                .map(franquicia -> mapper.toDTO(franquicia))
                .switchIfEmpty(Mono.error(new BusinessException(NOT_FOUND_FRANQUICIA)));
    }


    @Override
    public Mono<List<ProductoStockDTO>> getProductoConMayorStockPorSucursal(String franquiciaId) {
        return repository.findById(franquiciaId)
                .flatMapMany(franquicia -> Flux.fromIterable(franquicia.getSucursales()))
                .flatMap(sucursal ->  Mono.justOrEmpty(
                            sucursal.getProductos().stream()
                                    .max(Comparator.comparing(Producto::getCantidadStock))
                    ).map(productoConMayorStock ->
                            new ProductoStockDTO(sucursal.getNombre(), productoMapper.toDTO(productoConMayorStock))
                    )
                )
                .collectList();
    }

    @Override
    public Mono<FranquiciaDTO> updateNameFranquicia(String id, String name) {
        return repository.findById(id)
                .flatMap(franquicia -> {
                    franquicia.setNombre(name);
                    return repository.save(franquicia);
                })
                .map(updatedProducto -> mapper.toDTO(updatedProducto))
                .switchIfEmpty(Mono.error(new BusinessException(NOT_FOUND_FRANQUICIA)));
    }
}

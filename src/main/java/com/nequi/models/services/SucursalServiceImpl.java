package com.nequi.models.services;

import com.nequi.exception.BusinessException;
import com.nequi.models.documents.Producto;
import com.nequi.models.documents.Sucursal;
import com.nequi.models.dto.ProductoDTO;
import com.nequi.models.dto.SucursalDTO;
import com.nequi.models.mapper.ProductoMapper;
import com.nequi.models.mapper.SucursalMapper;
import com.nequi.models.repository.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class SucursalServiceImpl implements SucursalService{

    public static final String NOT_FOUND_SUCURSAL = "Not found sucursal";
    @Autowired
    private SucursalRepository repository;

    @Autowired
    private SucursalMapper mapper;

    @Autowired
    private ProductoMapper productoMapper;

    @Autowired
    private ProductoService productoService;


    @Override
    public Mono<SucursalDTO> create(String nombre) {
        Sucursal sucursal = mapper.toEntity(SucursalDTO.builder().nombre(nombre).build());
        return repository.save(sucursal).map(saveSucursal ->mapper.toDTO(saveSucursal));
    }

    @Override
    public Mono<SucursalDTO> createSucursalIntranet(SucursalDTO sucursalDTO) {
        Sucursal sucursal = mapper.toEntity(sucursalDTO);
        return repository.save(sucursal).map(saveSucursal ->mapper.toDTO(saveSucursal));
    }

    @Override
    public Mono<SucursalDTO> addNewProductToSucursal(String id, ProductoDTO productoDTO) {
        return repository.findById(id)
                .flatMap(sucursal ->
                    productoService.create(productoDTO)
                            .flatMap(nuevoProducto -> {
                                Producto productoEntidad = productoMapper.toEntity(nuevoProducto);

                                if (sucursal.getProductos() == null) {
                                    sucursal.setProductos(new ArrayList<>());
                                }

                                sucursal.getProductos().add(productoEntidad);

                                return repository.save(sucursal);
                            })
                )
                .map(sucursalActualizada -> mapper.toDTO(sucursalActualizada))
                .switchIfEmpty(Mono.error(new BusinessException(NOT_FOUND_SUCURSAL)));
    }


    @Override
    public Mono<SucursalDTO> removeProductFromSucursal(String id, String productoId) {
        return repository.findById(id)
                .flatMap(sucursal -> {
                    List<Producto> productos = sucursal.getProductos();

                    if (productos != null && !productos.isEmpty()) {
                        boolean removed = productos.removeIf(p -> p.getId().equals(productoId));

                        if (removed) {
                            return repository.save(sucursal)
                                    .map(sucActualizada -> mapper.toDTO(sucActualizada)); // Convertir a DTO
                        } else {
                            return Mono.error(new BusinessException("Producto no encontrado en la sucursal"));
                        }
                    } else {
                        return Mono.error(new BusinessException("No hay productos en la sucursal"));
                    }
                })
                .switchIfEmpty(Mono.error(new BusinessException("Sucursal no encontrada")));
    }

    @Override
    public Mono<SucursalDTO> updateNameSucursal(String id, String name) {
        return repository.findById(id)
                .flatMap(sucursal -> {
                    sucursal.setNombre(name);
                    return repository.save(sucursal);
                })
                .map(updatedProducto -> mapper.toDTO(updatedProducto))
                .switchIfEmpty(Mono.error(new BusinessException(NOT_FOUND_SUCURSAL)));
    }
}

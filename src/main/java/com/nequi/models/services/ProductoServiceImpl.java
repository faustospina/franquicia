package com.nequi.models.services;

import com.nequi.exception.BusinessException;
import com.nequi.models.documents.Producto;
import com.nequi.models.dto.ProductoDTO;
import com.nequi.models.mapper.ProductoMapper;
import com.nequi.models.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
public class ProductoServiceImpl implements ProductoService{

    public static final String PRODUCTO_NO_ENCONTRADO = "product not found";
    @Autowired
    private ProductoRepository repository;

    @Autowired
    private ProductoMapper mapper;

    @Override
    public Mono<ProductoDTO> create(ProductoDTO productoDTO) {
        Producto producto = mapper.toEntity(productoDTO);
        return repository.save(producto)
                .map(savedProducto -> mapper.toDTO(savedProducto));
    }

    @Override
    public Mono<ProductoDTO> updateNameProducto(String id, String name) {
        return repository.findById(id)
                .flatMap(producto -> {
                    producto.setNombre(name);
                    return repository.save(producto);
                })
                .map(updatedProducto -> mapper.toDTO(updatedProducto))
                .switchIfEmpty(Mono.error(new BusinessException(PRODUCTO_NO_ENCONTRADO.concat(id))));
    }

    @Override
    public Mono<ProductoDTO> updateStockProducto(String id, Integer stock) {
        return repository.findById(id)
                .flatMap(producto -> {
                    producto.setCantidadStock(stock);
                    return repository.save(producto);
                })
                .map(updatedProducto -> mapper.toDTO(updatedProducto))
                .switchIfEmpty(Mono.error(new BusinessException(PRODUCTO_NO_ENCONTRADO)));
    }
}

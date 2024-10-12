package com.nequi.models.services;

import com.nequi.exception.BusinessException;
import com.nequi.models.documents.Producto;
import com.nequi.models.dto.ProductoDTO;
import com.nequi.models.mapper.ProductoMapper;
import com.nequi.models.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {



    @InjectMocks
    private ProductoServiceImpl productoService;

    @Mock
    private ProductoMapper mapper;

    @Mock
    private ProductoRepository repository;

    private Producto producto;
    private ProductoDTO productoDTO;

    @BeforeEach
    void setUp() {
        producto = new Producto("1", "ProductoTest", 10);
        productoDTO = new ProductoDTO("1", "ProductoTest", 10);
    }

    @Test
    void testCreate() {
        when(mapper.toEntity(productoDTO)).thenReturn(producto);
        when(repository.save(producto)).thenReturn(Mono.just(producto));
        when(mapper.toDTO(producto)).thenReturn(productoDTO);

        StepVerifier.create(productoService.create(productoDTO))
                .expectNext(productoDTO)
                .verifyComplete();

        verify(repository).save(producto);
    }

    @Test
    void testUpdateNameProducto() {
        String newName = "ProductoActualizado";
        Producto updatedProducto = new Producto("1", newName, 10);
        ProductoDTO updatedProductoDTO = new ProductoDTO("1", newName, 10);

        when(repository.findById("1")).thenReturn(Mono.just(producto));
        when(repository.save(producto)).thenReturn(Mono.just(updatedProducto));
        when(mapper.toDTO(updatedProducto)).thenReturn(updatedProductoDTO);

        StepVerifier.create(productoService.updateNameProducto("1", newName))
                .expectNext(updatedProductoDTO)
                .verifyComplete();

        verify(repository).findById("1");
        verify(repository).save(producto);
    }

    @Test
    void testUpdateStockProducto() {
        Integer newStock = 20;
        Producto updatedProducto = new Producto("1", "ProductoTest", newStock);
        ProductoDTO updatedProductoDTO = new ProductoDTO("1", "ProductoTest", newStock);

        when(repository.findById("1")).thenReturn(Mono.just(producto));
        when(repository.save(producto)).thenReturn(Mono.just(updatedProducto));
        when(mapper.toDTO(updatedProducto)).thenReturn(updatedProductoDTO);

        StepVerifier.create(productoService.updateStockProducto("1", newStock))
                .expectNext(updatedProductoDTO)
                .verifyComplete();

        verify(repository).findById("1");
        verify(repository).save(producto);
    }

    @Test
    void testUpdateNameProducto_NotFound() {
        when(repository.findById("1")).thenReturn(Mono.empty());

        StepVerifier.create(productoService.updateNameProducto("1", "ProductoNuevo"))
                .expectErrorMatches(throwable ->
                        throwable instanceof BusinessException &&
                                throwable.getMessage().equals("product not found1")
                )
                .verify();

        verify(repository).findById("1");
    }

    @Test
    void testUpdateStockProducto_NotFound() {
        when(repository.findById("1")).thenReturn(Mono.empty());

        StepVerifier.create(productoService.updateStockProducto("1", 15))
                .expectErrorMatches(throwable ->
                        throwable instanceof BusinessException &&
                                throwable.getMessage().equals("product not found")
                )
                .verify();

        verify(repository).findById("1");
    }
}
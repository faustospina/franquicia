package com.nequi.models.services;

import com.nequi.exception.BusinessException;
import com.nequi.models.documents.Producto;
import com.nequi.models.documents.Sucursal;
import com.nequi.models.dto.ProductoDTO;
import com.nequi.models.dto.SucursalDTO;
import com.nequi.models.mapper.ProductoMapper;
import com.nequi.models.mapper.SucursalMapper;
import com.nequi.models.repository.SucursalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SucursalServiceImplTest {

    @Mock
    private SucursalRepository repository;

    @Mock
    private SucursalMapper mapper;

    @Mock
    private ProductoMapper productoMapper;

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private SucursalServiceImpl sucursalService;

    private Sucursal sucursal;
    private SucursalDTO sucursalDTO;
    private Producto producto;
    private ProductoDTO productoDTO;

    @BeforeEach
    void setUp() {
        sucursal = new Sucursal("1", "SucursalTest", new ArrayList<>());
        sucursalDTO = SucursalDTO.builder().id("1").nombre("SucursalTest").build();
        producto = new Producto("101", "ProductoTest", 10);
        productoDTO = new ProductoDTO("101", "ProductoTest", 10);
    }

    @Test
    void testCreate() {
        when(mapper.toEntity(any(SucursalDTO.class))).thenReturn(sucursal);
        when(repository.save(sucursal)).thenReturn(Mono.just(sucursal));
        when(mapper.toDTO(sucursal)).thenReturn(sucursalDTO);

        StepVerifier.create(sucursalService.create("SucursalTest"))
                .expectNext(sucursalDTO)
                .verifyComplete();

        verify(repository).save(sucursal);
    }

    @Test
    void testCreateSucursalIntranet() {
        when(mapper.toEntity(sucursalDTO)).thenReturn(sucursal);
        when(repository.save(sucursal)).thenReturn(Mono.just(sucursal));
        when(mapper.toDTO(sucursal)).thenReturn(sucursalDTO);

        StepVerifier.create(sucursalService.createSucursalIntranet(sucursalDTO))
                .expectNext(sucursalDTO)
                .verifyComplete();

        verify(repository).save(sucursal);
    }

    @Test
    void testAddNewProductToSucursal() {
        when(repository.findById("1")).thenReturn(Mono.just(sucursal));
        when(productoService.create(productoDTO)).thenReturn(Mono.just(productoDTO));
        when(productoMapper.toEntity(productoDTO)).thenReturn(producto);
        when(repository.save(sucursal)).thenReturn(Mono.just(sucursal));
        when(mapper.toDTO(sucursal)).thenReturn(sucursalDTO);

        StepVerifier.create(sucursalService.addNewProductToSucursal("1", productoDTO))
                .expectNext(sucursalDTO)
                .verifyComplete();

        verify(repository).save(sucursal);
    }

    @Test
    void testAddNewProductToSucursal_NotFound() {
        when(repository.findById("1")).thenReturn(Mono.empty());

        StepVerifier.create(sucursalService.addNewProductToSucursal("1", productoDTO))
                .expectErrorMatches(throwable ->
                        throwable instanceof BusinessException &&
                                throwable.getMessage().equals("Not found sucursal")
                )
                .verify();

        verify(repository).findById("1");
    }

    @Test
    void testRemoveProductFromSucursal() {
        sucursal.getProductos().add(producto);

        when(repository.findById("1")).thenReturn(Mono.just(sucursal));
        when(repository.save(sucursal)).thenReturn(Mono.just(sucursal));
        when(mapper.toDTO(sucursal)).thenReturn(sucursalDTO);

        StepVerifier.create(sucursalService.removeProductFromSucursal("1", "101"))
                .expectNext(sucursalDTO)
                .verifyComplete();

        verify(repository).save(sucursal);
    }

    @Test
    void testRemoveProductFromSucursal_NotFound() {
        when(repository.findById("1")).thenReturn(Mono.just(sucursal));

        StepVerifier.create(sucursalService.removeProductFromSucursal("1", "999"))
                .expectErrorMatches(throwable ->
                        throwable instanceof BusinessException &&
                                throwable.getMessage().equals("No hay productos en la sucursal")
                )
                .verify();

        verify(repository).findById("1");
    }

    @Test
    void testRemoveProductFromSucursal_NoProducts() {
        when(repository.findById("1")).thenReturn(Mono.just(new Sucursal("1", "SucursalTest", new ArrayList<>())));

        StepVerifier.create(sucursalService.removeProductFromSucursal("1", "101"))
                .expectErrorMatches(throwable ->
                        throwable instanceof BusinessException &&
                                throwable.getMessage().equals("No hay productos en la sucursal")
                )
                .verify();

        verify(repository).findById("1");
    }

    @Test
    void testUpdateNameSucursal() {
        String newName = "SucursalActualizada";
        Sucursal updatedSucursal = new Sucursal("1", newName, new ArrayList<>());

        when(repository.findById("1")).thenReturn(Mono.just(sucursal));
        when(repository.save(sucursal)).thenReturn(Mono.just(updatedSucursal));
        when(mapper.toDTO(updatedSucursal)).thenReturn(SucursalDTO.builder().id("1").nombre(newName).build());

        StepVerifier.create(sucursalService.updateNameSucursal("1", newName))
                .expectNextMatches(dto -> dto.getNombre().equals(newName))
                .verifyComplete();

        verify(repository).save(sucursal);
    }

    @Test
    void testUpdateNameSucursal_NotFound() {
        when(repository.findById("1")).thenReturn(Mono.empty());

        StepVerifier.create(sucursalService.updateNameSucursal("1", "SucursalNueva"))
                .expectErrorMatches(throwable ->
                        throwable instanceof BusinessException &&
                                throwable.getMessage().equals("Not found sucursal")
                )
                .verify();

        verify(repository).findById("1");
    }
}
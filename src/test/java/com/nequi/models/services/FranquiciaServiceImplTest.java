package com.nequi.models.services;

import com.nequi.exception.BusinessException;
import com.nequi.models.documents.Franquicia;
import com.nequi.models.documents.Producto;
import com.nequi.models.documents.Sucursal;
import com.nequi.models.dto.FranquiciaDTO;
import com.nequi.models.dto.ProductoDTO;
import com.nequi.models.dto.SucursalDTO;
import com.nequi.models.mapper.FranquiciaMapper;
import com.nequi.models.mapper.ProductoMapper;
import com.nequi.models.mapper.SucursalMapper;
import com.nequi.models.repository.FranquiciaRespository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FranquiciaServiceImplTest {
    @Mock
    private FranquiciaRespository repository;

    @Mock
    private FranquiciaMapper franquiciaMapper;

    @Mock
    private SucursalMapper sucursalMapper;

    @Mock
    private ProductoMapper productoMapper;

    @Mock
    private SucursalService sucursalService;

    @InjectMocks
    private FranquiciaServiceImpl franquiciaService;

    private Franquicia franquicia;
    private FranquiciaDTO franquiciaDTO;
    private Sucursal sucursal;
    private SucursalDTO sucursalDTO;
    private Producto producto;
    private ProductoDTO productoDTO;

    @BeforeEach
    void setUp() {
        franquicia = new Franquicia("1", "FranquiciaTest", new ArrayList<>());
        franquiciaDTO = FranquiciaDTO.builder().id("1").nombre("FranquiciaTest").build();
        sucursal = new Sucursal("101", "SucursalTest", new ArrayList<>());
        sucursalDTO = SucursalDTO.builder().id("101").nombre("SucursalTest").build();
        producto = new Producto("201", "ProductoTest", 50);
        productoDTO = new ProductoDTO("201", "ProductoTest", 50);
    }

    @Test
    void testCreate() {
        when(franquiciaMapper.toEntity(any(FranquiciaDTO.class))).thenReturn(franquicia);
        when(repository.save(franquicia)).thenReturn(Mono.just(franquicia));
        when(franquiciaMapper.toDTO(franquicia)).thenReturn(franquiciaDTO);

        StepVerifier.create(franquiciaService.create("FranquiciaTest"))
                .expectNext(franquiciaDTO)
                .verifyComplete();

        verify(repository).save(franquicia);
    }

    @Test
    void testAddSucursalToFranquicia() {
        when(repository.findById("1")).thenReturn(Mono.just(franquicia));
        when(sucursalService.createSucursalIntranet(sucursalDTO)).thenReturn(Mono.just(sucursalDTO));
        when(sucursalMapper.toEntity(sucursalDTO)).thenReturn(sucursal);
        when(repository.save(franquicia)).thenReturn(Mono.just(franquicia));
        when(franquiciaMapper.toDTO(franquicia)).thenReturn(franquiciaDTO);

        StepVerifier.create(franquiciaService.addSucursalToFranquicia("1", sucursalDTO))
                .expectNext(franquiciaDTO)
                .verifyComplete();

        verify(repository).save(franquicia);
    }

    @Test
    void testAddSucursalToFranquicia_NotFound() {
        when(repository.findById("1")).thenReturn(Mono.empty());

        StepVerifier.create(franquiciaService.addSucursalToFranquicia("1", sucursalDTO))
                .expectErrorMatches(throwable ->
                        throwable instanceof BusinessException &&
                                throwable.getMessage().equals("Not found franquicia")
                )
                .verify();

        verify(repository).findById("1");
    }

    @Test
    void testUpdateNameFranquicia() {
        String newName = "FranquiciaActualizada";
        Franquicia updatedFranquicia = new Franquicia("1", newName, new ArrayList<>());

        when(repository.findById("1")).thenReturn(Mono.just(franquicia));
        when(repository.save(franquicia)).thenReturn(Mono.just(updatedFranquicia));
        when(franquiciaMapper.toDTO(updatedFranquicia)).thenReturn(FranquiciaDTO.builder().id("1").nombre(newName).build());

        StepVerifier.create(franquiciaService.updateNameFranquicia("1", newName))
                .expectNextMatches(dto -> dto.getNombre().equals(newName))
                .verifyComplete();

        verify(repository).save(franquicia);
    }

    @Test
    void testUpdateNameFranquicia_NotFound() {
        when(repository.findById("1")).thenReturn(Mono.empty());

        StepVerifier.create(franquiciaService.updateNameFranquicia("1", "FranquiciaNueva"))
                .expectErrorMatches(throwable ->
                        throwable instanceof BusinessException &&
                                throwable.getMessage().equals("Not found franquicia")
                )
                .verify();

        verify(repository).findById("1");
    }


    @Test
    void testGetProductoConMayorStockPorSucursal() {
        // Datos de prueba
        Producto producto1 = Producto.builder()
                .id("1")
                .nombre("Producto A")
                .cantidadStock(30)
                .build();

        Producto producto2 = Producto.builder()
                .id("2")
                .nombre("Producto B")
                .cantidadStock(50)
                .build();

        Sucursal sucursal = Sucursal.builder()
                .id("101")
                .nombre("Sucursal 1")
                .productos(List.of(producto1, producto2))
                .build();

        Franquicia franquicia = Franquicia.builder()
                .id("1")
                .nombre("Franquicia 1")
                .sucursales(List.of(sucursal))
                .build();

        ProductoDTO productoDTO = ProductoDTO.builder()
                .id("2")
                .nombre("Producto B")
                .cantidadStock(50)
                .build();

        // Mocking
        when(repository.findById("1")).thenReturn(Mono.just(franquicia));
        when(productoMapper.toDTO(producto2)).thenReturn(productoDTO);

        // Verificación del flujo
        StepVerifier.create(franquiciaService.getProductoConMayorStockPorSucursal("1"))
                .expectNextMatches(productoStockList ->
                        productoStockList.size() == 1 &&
                                productoStockList.get(0).getSucursalNombre().equals("Sucursal 1") &&
                                productoStockList.get(0).getProductoConMayorStock().getCantidadStock()==50
                )
                .verifyComplete();

        // Verificación de que se llamó a los métodos correctos
        verify(repository).findById("1");
    }

    @Test
    void testGetProductoConMayorStockPorSucursal_EmptySucursales() {
        Franquicia franquicia = Franquicia.builder()
                .id("1")
                .nombre("Franquicia 1")
                .sucursales(new ArrayList<>())
                .build();

        when(repository.findById("1")).thenReturn(Mono.just(franquicia));

        StepVerifier.create(franquiciaService.getProductoConMayorStockPorSucursal("1"))
                .expectNextMatches(List::isEmpty)
                .verifyComplete();

        verify(repository).findById("1");
    }

    @Test
    void testGetProductoConMayorStockPorSucursal_NotFound() {
        when(repository.findById("1")).thenReturn(Mono.empty());

        StepVerifier.create(franquiciaService.getProductoConMayorStockPorSucursal("1"))
                .expectNextMatches(List::isEmpty)
                .verifyComplete();

        verify(repository).findById("1");
    }
}
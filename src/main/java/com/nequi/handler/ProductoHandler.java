package com.nequi.handler;

import com.nequi.models.dto.ProductoDTO;
import com.nequi.models.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.*;
@Component
public class ProductoHandler {

    @Autowired
    private ProductoService service;

    public Mono<ServerResponse> create(ServerRequest request){
        return request.bodyToMono(ProductoDTO.class)
                .flatMap(productoDTO->service.create(productoDTO).flatMap(productoOut->ServerResponse
                .created(URI.create("/producto".concat(productoOut.getId())))
                .contentType(APPLICATION_JSON)
                .body(fromObject(productoOut))
                        ));
    }

    public Mono<ServerResponse> updateNameProducto(ServerRequest request) {
        String id = request.pathVariable("id");
        return request.bodyToMono(ProductoDTO.class)
                .flatMap(productoDTO -> service.updateNameProducto(id, productoDTO.getNombre())
                        .flatMap(updatedProducto -> ServerResponse
                                .ok()
                                .contentType(APPLICATION_JSON)
                                .body(fromObject(updatedProducto))
                        )
                        .switchIfEmpty(ServerResponse.notFound().build())
                );
    }

    public Mono<ServerResponse> updateStockProducto(ServerRequest request) {
        String id = request.pathVariable("id");
        return request.bodyToMono(ProductoDTO.class)
                .flatMap(productoDTO -> service.updateStockProducto(id, productoDTO.getCantidadStock())
                        .flatMap(updatedProducto -> ServerResponse
                                .ok()
                                .contentType(APPLICATION_JSON)
                                .body(fromObject(updatedProducto))
                        )
                        .switchIfEmpty(ServerResponse.notFound().build())
                );
    }

}

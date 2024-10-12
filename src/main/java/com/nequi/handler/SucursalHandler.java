package com.nequi.handler;
import com.nequi.models.dto.ProductoDTO;
import com.nequi.models.dto.SucursalDTO;
import com.nequi.models.services.SucursalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.*;
@Component
public class SucursalHandler {

    @Autowired
    private SucursalService service;

    public Mono<ServerResponse> create(ServerRequest request){
        return request.bodyToMono(SucursalDTO.class)
                .flatMap(sucursalDTO->service.create(sucursalDTO.getNombre()).flatMap(productoOut->ServerResponse
                        .created(URI.create("/sucursal".concat(productoOut.getId())))
                        .contentType(APPLICATION_JSON)
                        .body(fromObject(productoOut))
                ));
    }

    public Mono<ServerResponse> addNewProductToSucursal(ServerRequest request) {
        String id = request.pathVariable("id");
        return request.bodyToMono(ProductoDTO.class)
                .flatMap(productoDTO -> service.addNewProductToSucursal(id, productoDTO)
                        .flatMap(updatedSucursal -> ServerResponse
                                .ok()
                                .contentType(APPLICATION_JSON)
                                .body(fromObject(updatedSucursal))
                        )
                        .switchIfEmpty(ServerResponse.notFound().build())
                );
    }

    public Mono<ServerResponse> removeProductFromSucursal(ServerRequest request) {
        String id = request.pathVariable("id");
        String productoId = request.pathVariable("productoId");
        return service.removeProductFromSucursal(id, productoId)
                .flatMap(updatedSucursal -> ServerResponse
                        .ok()
                        .contentType(APPLICATION_JSON)
                        .body(fromObject(updatedSucursal))
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> updateNameSucursal(ServerRequest request) {
        String id = request.pathVariable("id");
        return request.bodyToMono(SucursalDTO.class)
                .flatMap(sucursalDto -> service.updateNameSucursal(id, sucursalDto.getNombre()))
                .flatMap(updateSucursal -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(updateSucursal))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(e -> ServerResponse.status(HttpStatus.BAD_REQUEST)
                        .bodyValue(e.getMessage()));
    }

}

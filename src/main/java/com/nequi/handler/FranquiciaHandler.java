package com.nequi.handler;

import com.nequi.models.dto.FranquiciaDTO;
import com.nequi.models.dto.SucursalDTO;
import com.nequi.models.services.FranquiciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class FranquiciaHandler {

    @Autowired
    private FranquiciaService service;

    public Mono<ServerResponse> createFranquicia(ServerRequest request) {
        return request.bodyToMono(FranquiciaDTO.class)
                .flatMap(franquiciaDTO->service.create(franquiciaDTO.getNombre()).flatMap(franquiciaOut->ServerResponse
                        .created(URI.create("/franquicia".concat(franquiciaOut.getId())))
                        .contentType(APPLICATION_JSON)
                        .body(fromObject(franquiciaOut))
                ));
    }

    public Mono<ServerResponse> addSucursalToFranquicia(ServerRequest request) {
        String franquiciaId = request.pathVariable("id");

        return request.bodyToMono(SucursalDTO.class)
                .flatMap(sucursalDTO -> service.addSucursalToFranquicia(franquiciaId, sucursalDTO))
                .flatMap(franquiciaDTO -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(franquiciaDTO))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(e -> ServerResponse.status(HttpStatus.BAD_REQUEST)
                        .bodyValue(e.getMessage()));
    }

    public Mono<ServerResponse> getProductoConMayorStockPorSucursal(ServerRequest request) {
        String id = request.pathVariable("id");

        return service.getProductoConMayorStockPorSucursal(id)
                .flatMap(productoList -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(productoList))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(e -> ServerResponse.status(HttpStatus.BAD_REQUEST)
                        .bodyValue(e.getMessage()));
    }

    public Mono<ServerResponse> updateNameFranquicia(ServerRequest request) {
        String id = request.pathVariable("id");

        return request.bodyToMono(FranquiciaDTO.class)
                .flatMap(franquiciaDTO -> service.updateNameFranquicia(id, franquiciaDTO.getNombre()))
                .flatMap(updatedFranquicia -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(updatedFranquicia))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(e -> ServerResponse.status(HttpStatus.BAD_REQUEST)
                        .bodyValue(e.getMessage()));
    }

}

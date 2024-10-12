package com.nequi;
import com.nequi.handler.FranquiciaHandler;
import com.nequi.handler.ProductoHandler;
import com.nequi.handler.SucursalHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouterFunctionConfig {
    @Bean
    public RouterFunction<ServerResponse> routesProducto(ProductoHandler handler){
        return route(POST("producto"),handler::create)
                .andRoute(PUT("producto/{id}/nombre"),handler::updateNameProducto)
                .andRoute(PUT("producto/stock/{id}"),handler::updateStockProducto);
    }

    @Bean
    public RouterFunction<ServerResponse> routesSucursal(SucursalHandler handler) {
        return route(POST("sucursal"), handler::create)
                .andRoute(PUT("sucursal/{id}/add-product"), handler::addNewProductToSucursal)
                .andRoute(DELETE("sucursal/{id}/remove-product/{productoId}"), handler::removeProductFromSucursal)
                .andRoute(PUT("sucursal/{id}/nombre"), handler::updateNameSucursal);
    }

    @Bean
    public RouterFunction<ServerResponse> routesFranquicia(FranquiciaHandler handler) {
        return route()
                .POST("franquicia", handler::createFranquicia)
                .PUT("franquicia/{id}/add-sucursal", handler::addSucursalToFranquicia)
                .GET("franquicia/{id}/productos/max-stock", handler::getProductoConMayorStockPorSucursal)
                .PUT("franquicia/{id}/nombre", handler::updateNameFranquicia)
                .build();
    }


}

package com.nequi.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoStockDTO {
    private String sucursalNombre;
    private ProductoDTO productoConMayorStock;
}

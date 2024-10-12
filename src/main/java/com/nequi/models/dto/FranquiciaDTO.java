package com.nequi.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FranquiciaDTO {
    private String id;
    private String nombre;
    private List<SucursalDTO> sucursales;
}

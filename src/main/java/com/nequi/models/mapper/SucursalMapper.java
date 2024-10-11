package com.nequi.models.mapper;

import com.nequi.models.documents.Sucursal;
import com.nequi.models.dto.SucursalDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SucursalMapper {
    SucursalDTO toDTO(Sucursal entity);
    Sucursal toEntity(SucursalDTO dto);
}

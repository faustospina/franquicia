package com.nequi.models.mapper;

import com.nequi.models.documents.Producto;
import com.nequi.models.dto.ProductoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductoMapper {

    Producto toEntity(ProductoDTO dto);
    ProductoDTO toDTO(Producto entity);

}

package com.nequi.models.mapper;

import com.nequi.models.documents.Franquicia;
import com.nequi.models.dto.FranquiciaDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FranquiciaMapper {
    FranquiciaDTO toDTO(Franquicia entity);
    Franquicia toEntity(FranquiciaDTO dto);
}

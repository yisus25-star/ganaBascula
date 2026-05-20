package com.ganabascula.mapper;

import com.ganabascula.dto.request.TransaccionRequestDto;
import com.ganabascula.dto.response.CategoriaAnimalResponseDto;
import com.ganabascula.dto.response.GastoAdicionalResponseDto;
import com.ganabascula.dto.response.TransaccionResponseDto;
import com.ganabascula.entity.CategoriaAnimal;
import com.ganabascula.entity.GastoAdicional;
import com.ganabascula.entity.Transaccion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransaccionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "totalBruto", ignore = true)
    @Mapping(target = "totalNeto", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "categorias", ignore = true)
    @Mapping(target = "gastos", ignore = true)
    Transaccion toEntity(TransaccionRequestDto dto);

    TransaccionResponseDto toDto(Transaccion transaccion);

    CategoriaAnimalResponseDto categoriaToDto(CategoriaAnimal categoria);

    GastoAdicionalResponseDto gastoToDto(GastoAdicional gasto);
}
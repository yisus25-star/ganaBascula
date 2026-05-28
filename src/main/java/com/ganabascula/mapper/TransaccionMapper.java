package com.ganabascula.mapper;

import com.ganabascula.dto.request.TransaccionRequestDto;

import com.ganabascula.dto.response.CategoriaAnimalResponseDto;
import com.ganabascula.dto.response.GastoAdicionalResponseDto;
import com.ganabascula.dto.response.TransaccionResponseDto;

import com.ganabascula.entity.DetalleTransaccionAnimal;
import com.ganabascula.entity.GastoAdicional;
import com.ganabascula.entity.Transaccion;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransaccionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "totalBruto", ignore = true)
    @Mapping(target = "totalNeto", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "detalles", ignore = true)
    @Mapping(target = "gastos", ignore = true)
    Transaccion toEntity(
            TransaccionRequestDto dto
    );

    @Mapping(target = "categorias", source = "detalles")
    @Mapping(target = "gastos", source = "gastos")
    TransaccionResponseDto toDto(
            Transaccion transaccion
    );

    List<CategoriaAnimalResponseDto> mapDetalles(
            List<DetalleTransaccionAnimal> detalles
    );

    List<GastoAdicionalResponseDto> mapGastos(
            List<GastoAdicional> gastos
    );

    CategoriaAnimalResponseDto detalleToDto(
            DetalleTransaccionAnimal detalle
    );

    GastoAdicionalResponseDto gastoToDto(
            GastoAdicional gasto
    );
}
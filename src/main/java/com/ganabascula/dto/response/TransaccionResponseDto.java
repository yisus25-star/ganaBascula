package com.ganabascula.dto.response;

import com.ganabascula.entity.Transaccion.EstadoTransaccion;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class TransaccionResponseDto {

    private Long id;

    private String nombreComprador;

    private LocalDate fecha;

    private EstadoTransaccion estado;

    private BigDecimal totalBruto;

    private BigDecimal totalNeto;

    private String observaciones;

    private LocalDateTime fechaRegistro;

    private List<CategoriaAnimalResponseDto>
            categorias;

    private List<GastoAdicionalResponseDto>
            gastos;
}
package com.ganabascula.dto.response;

import com.ganabascula.entity.GastoAdicional.TipoGasto;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class GastoAdicionalResponseDto {

    private Long id;
    private TipoGasto tipo;
    private String descripcion;
    private BigDecimal valor;
    private Boolean aplica;
}
package com.ganabascula.dto.request;

import com.ganabascula.entity.GastoAdicional.TipoGasto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class GastoAdicionalRequestDto {

    @NotNull(message = "El tipo de gasto es obligatorio")
    private TipoGasto tipo;

    private String descripcion;

    @NotNull(message = "El valor es obligatorio")
    @Positive(message = "El valor debe ser mayor a 0")
    private BigDecimal valor;
}
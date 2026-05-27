package com.ganabascula.dto.request;

import com.ganabascula.entity.GastoAdicional.TipoGasto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GastoAdicionalRequestDto {

    @NotNull(message = "El tipo de gasto es obligatorio")
    private TipoGasto tipo;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @NotNull(message = "El valor es obligatorio")
    @Positive(message = "El valor debe ser mayor a 0")
    private BigDecimal valor;

    private Boolean aplica = true;
}
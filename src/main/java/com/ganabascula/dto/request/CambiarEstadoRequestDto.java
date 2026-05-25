package com.ganabascula.dto.request;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class CambiarEstadoRequestDto {

    @NotBlank(message = "El estado es obligatorio")
    private String estado;
}
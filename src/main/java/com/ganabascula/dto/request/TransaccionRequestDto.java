package com.ganabascula.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TransaccionRequestDto {

    @NotBlank(message = "El nombre del comprador es obligatorio")
    @Size(min = 3, message = "El nombre debe tener mínimo 3 caracteres")
    private String nombreComprador;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @Size(max = 255, message = "Las observaciones no pueden superar 255 caracteres")
    private String observaciones;
}
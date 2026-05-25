package com.ganabascula.dto.request;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReporteRequestDto {

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDate fechaFin;
}
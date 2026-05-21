package com.ganabascula.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReporteRequestDto {

    private LocalDate fechaInicio;

    private LocalDate fechaFin;
}
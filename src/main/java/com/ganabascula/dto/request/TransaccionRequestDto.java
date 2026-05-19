package com.ganabascula.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class TransaccionRequestDto {

    @NotBlank(message = "El nombre del comprador es obligatorio")
    private String nombreComprador;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    private String observaciones;
}
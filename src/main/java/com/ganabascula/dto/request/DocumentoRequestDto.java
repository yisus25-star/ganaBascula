package com.ganabascula.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

import org.springframework.web.multipart.MultipartFile;

@Data
public class DocumentoRequestDto {

    @NotNull(message = "El archivo es obligatorio")
    private MultipartFile archivo;

    @NotBlank(message = "El tipo de documento es obligatorio")
    private String tipoDocumento;

    private String observaciones;
}
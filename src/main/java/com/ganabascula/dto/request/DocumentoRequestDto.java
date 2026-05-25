package com.ganabascula.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class DocumentoRequestDto {

    private MultipartFile archivo;

    @NotBlank(message = "El tipo de documento es obligatorio")
    private String tipoDocumento;

    private String observaciones;
}
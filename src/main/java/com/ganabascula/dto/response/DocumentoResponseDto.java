package com.ganabascula.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentoResponseDto {

    private Long id;

    private String nombreArchivo;

    private String tipoDocumento;

    private String rutaArchivo;

    private String estado;

    private String observaciones;
}
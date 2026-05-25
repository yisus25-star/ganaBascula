package com.ganabascula.service;

import com.ganabascula.dto.request.DocumentoRequestDto;
import com.ganabascula.dto.response.DocumentoResponseDto;

import java.util.List;

public interface ServiceDocumento {

    DocumentoResponseDto subirDocumento(
            DocumentoRequestDto dto
    );

    List<DocumentoResponseDto> listarDocumentos();

    DocumentoResponseDto aprobarDocumento(Long id);

    DocumentoResponseDto rechazarDocumento(Long id);
}
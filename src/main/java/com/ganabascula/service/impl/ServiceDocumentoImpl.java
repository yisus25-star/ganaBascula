package com.ganabascula.service.impl;

import com.ganabascula.dto.request.DocumentoRequestDto;
import com.ganabascula.dto.response.DocumentoResponseDto;
import com.ganabascula.service.ServiceDocumento;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceDocumentoImpl implements ServiceDocumento {

    @Override
    public DocumentoResponseDto subirDocumento(
            DocumentoRequestDto dto
    ) {

        try {

            Path carpetaUploads =
                    Paths.get("uploads");

            if (!Files.exists(carpetaUploads)) {

                Files.createDirectories(carpetaUploads);
            }

            String nombreArchivo =
                    dto.getArchivo().getOriginalFilename();

            Path rutaArchivo =
                    carpetaUploads.resolve(nombreArchivo);

            Files.copy(
                    dto.getArchivo().getInputStream(),
                    rutaArchivo,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return new DocumentoResponseDto(
                    1L,
                    nombreArchivo,
                    dto.getTipoDocumento(),
                    rutaArchivo.toString(),
                    "PENDIENTE",
                    dto.getObservaciones()
            );

        } catch (IOException e) {

            throw new RuntimeException(
                    "Error al subir documento"
            );
        }
    }

    @Override
    public List<DocumentoResponseDto> listarDocumentos() {

        return List.of(

                new DocumentoResponseDto(
                        1L,
                        "ica.pdf",
                        "ICA",
                        "uploads/ica.pdf",
                        "PENDIENTE",
                        "Documento cargado correctamente"
                )

        );
    }

    @Override
    public DocumentoResponseDto aprobarDocumento(Long id) {

        return new DocumentoResponseDto(
                id,
                "ica.pdf",
                "ICA",
                "uploads/ica.pdf",
                "APROBADO",
                "Documento aprobado por administrador"
        );
    }

    @Override
    public DocumentoResponseDto rechazarDocumento(Long id) {

        return new DocumentoResponseDto(
                id,
                "ica.pdf",
                "ICA",
                "uploads/ica.pdf",
                "RECHAZADO",
                "Documento rechazado por administrador"
        );
    }
}
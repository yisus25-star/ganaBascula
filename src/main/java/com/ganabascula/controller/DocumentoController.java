package com.ganabascula.controller;

import com.ganabascula.dto.request.DocumentoRequestDto;

import com.ganabascula.dto.response.ApiResponse;
import com.ganabascula.dto.response.DocumentoResponseDto;

import com.ganabascula.service.ServiceDocumento;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/documentos")
@RequiredArgsConstructor
public class DocumentoController {

    private final ServiceDocumento serviceDocumento;

    @PostMapping("/subir")
    @PreAuthorize("hasAnyRole('GANADERO', 'ADMIN')")
    public ResponseEntity<ApiResponse<DocumentoResponseDto>>
    subirDocumento(

            @ModelAttribute DocumentoRequestDto dto
    ) {

        DocumentoResponseDto response =

                serviceDocumento.subirDocumento(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)

                .body(

                        new ApiResponse<>(

                                "Documento subido correctamente",

                                response
                        )
                );
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<DocumentoResponseDto>>>
    listarDocumentos() {

        List<DocumentoResponseDto> response =

                serviceDocumento.listarDocumentos();

        return ResponseEntity.ok(

                new ApiResponse<>(

                        "Documentos obtenidos correctamente",

                        response
                )
        );
    }

    @PatchMapping("/{id}/aprobar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DocumentoResponseDto>>
    aprobarDocumento(
            @PathVariable Long id
    ) {

        DocumentoResponseDto response =

                serviceDocumento.aprobarDocumento(id);

        return ResponseEntity.ok(

                new ApiResponse<>(

                        "Documento aprobado correctamente",

                        response
                )
        );
    }

    @PatchMapping("/{id}/rechazar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DocumentoResponseDto>>
    rechazarDocumento(
            @PathVariable Long id
    ) {

        DocumentoResponseDto response =

                serviceDocumento.rechazarDocumento(id);

        return ResponseEntity.ok(

                new ApiResponse<>(

                        "Documento rechazado correctamente",

                        response
                )
        );
    }
}
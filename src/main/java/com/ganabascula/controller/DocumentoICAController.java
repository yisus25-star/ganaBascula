package com.ganabascula.controller;

import com.ganabascula.entity.DocumentoICA;

import com.ganabascula.service.ServiceDocumentoICA;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/documentos-ica")
public class DocumentoICAController {

    private final ServiceDocumentoICA
            serviceDocumentoICA;

    public DocumentoICAController(
            ServiceDocumentoICA serviceDocumentoICA
    ) {

        this.serviceDocumentoICA =
                serviceDocumentoICA;
    }

    @PostMapping("/subir/{transaccionId}")
    public ResponseEntity<DocumentoICA> subirDocumento(

            @PathVariable Long transaccionId,

            @RequestParam("archivo")
            MultipartFile archivo,

            Authentication auth
    ) {

        DocumentoICA documentoICA =
                serviceDocumentoICA.subirDocumento(
                        transaccionId,
                        archivo,
                        auth.getName()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(documentoICA);
    }
}
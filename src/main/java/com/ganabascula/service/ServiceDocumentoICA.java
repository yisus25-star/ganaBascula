package com.ganabascula.service;

import com.ganabascula.entity.DocumentoICA;

import org.springframework.web.multipart.MultipartFile;

public interface ServiceDocumentoICA {

    DocumentoICA subirDocumento(
            Long transaccionId,
            MultipartFile archivo,
            String cedula
    );
}
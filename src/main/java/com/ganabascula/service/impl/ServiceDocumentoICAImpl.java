package com.ganabascula.service.impl;

import com.ganabascula.entity.DocumentoICA;
import com.ganabascula.entity.Transaccion;
import com.ganabascula.entity.Usuario;

import com.ganabascula.repository.DocumentoICARepository;
import com.ganabascula.repository.TransaccionRepository;
import com.ganabascula.repository.UsuarioRepository;

import com.ganabascula.service.ServiceDocumentoICA;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ServiceDocumentoICAImpl
        implements ServiceDocumentoICA {

    private final DocumentoICARepository
            documentoICARepository;

    private final TransaccionRepository
            transaccionRepository;

    private final UsuarioRepository
            usuarioRepository;

    public ServiceDocumentoICAImpl(

            DocumentoICARepository
                    documentoICARepository,

            TransaccionRepository
                    transaccionRepository,

            UsuarioRepository
                    usuarioRepository
    ) {

        this.documentoICARepository =
                documentoICARepository;

        this.transaccionRepository =
                transaccionRepository;

        this.usuarioRepository =
                usuarioRepository;
    }

    @Override
    @Transactional
    public DocumentoICA subirDocumento(
            Long transaccionId,
            MultipartFile archivo,
            String cedula
    ) {

        Usuario usuario = usuarioRepository
                .findByCedula(cedula)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuario no encontrado"
                        )
                );

        Transaccion transaccion =
                transaccionRepository
                        .findById(transaccionId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Transaccion no encontrada"
                                )
                        );

        try {

            String carpeta =
                    "uploads/";

            Files.createDirectories(
                    Paths.get(carpeta)
            );

            String nombreArchivo =
                    System.currentTimeMillis()
                            + "_"
                            + archivo.getOriginalFilename();

            Path rutaArchivo =
                    Paths.get(carpeta, nombreArchivo);

            Files.copy(
                    archivo.getInputStream(),
                    rutaArchivo
            );

            DocumentoICA documentoICA =
                    DocumentoICA.builder()

                            .nombreArchivo(
                                    nombreArchivo
                            )

                            .tipoArchivo(
                                    archivo.getContentType()
                            )

                            .rutaArchivo(
                                    rutaArchivo.toString()
                            )

                            .usuario(usuario)

                            .transaccion(transaccion)

                            .build();

            return documentoICARepository
                    .save(documentoICA);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Error al subir archivo ICA"
            );
        }
    }
}
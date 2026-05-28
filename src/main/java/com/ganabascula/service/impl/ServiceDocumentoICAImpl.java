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

import java.util.List;

@Service
public class ServiceDocumentoICAImpl
        implements ServiceDocumentoICA {

    private final DocumentoICARepository
            documentoICARepository;

    private final TransaccionRepository
            transaccionRepository;

    private final UsuarioRepository
            usuarioRepository;

    private static final long
            MAX_FILE_SIZE = 5 * 1024 * 1024;

    private static final List<String>
            TIPOS_PERMITIDOS = List.of(

            "application/pdf",

            "image/png",

            "image/jpeg"
    );

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

        if (archivo.isEmpty()) {

            throw new RuntimeException(
                    "Debe seleccionar un archivo"
            );
        }

        if (
                archivo.getSize()
                        > MAX_FILE_SIZE
        ) {

            throw new RuntimeException(
                    "El archivo supera el tamaño máximo permitido de 5MB"
            );
        }

        if (
                !TIPOS_PERMITIDOS.contains(
                        archivo.getContentType()
                )
        ) {

            throw new RuntimeException(
                    "Tipo de archivo no permitido"
            );
        }

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

        if (
                !transaccion.getUsuario()
                        .getId()
                        .equals(usuario.getId())
        ) {

            throw new RuntimeException(
                    "No tienes permiso para subir archivos a esta transacción"
            );
        }

        try {

            String carpeta =
                    "uploads/";

            Files.createDirectories(
                    Paths.get(carpeta)
            );

            String nombreOriginal =
                    archivo.getOriginalFilename()
                            .replaceAll("\\s+", "_");

            String nombreArchivo =

                    System.currentTimeMillis()
                            + "_"
                            + nombreOriginal;

            Path rutaArchivo =

                    Paths.get(
                            carpeta,
                            nombreArchivo
                    );

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
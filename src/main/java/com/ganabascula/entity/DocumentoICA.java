package com.ganabascula.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "documentos_ica")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentoICA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "nombre_archivo",
            nullable = false
    )
    private String nombreArchivo;

    @Column(
            name = "tipo_archivo",
            nullable = false
    )
    private String tipoArchivo;

    @Column(
            name = "ruta_archivo",
            nullable = false
    )
    private String rutaArchivo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoDocumentoICA estado;

    @Column(
            name = "fecha_subida",
            nullable = false
    )
    private LocalDateTime fechaSubida;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "usuario_id",
            nullable = false
    )
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "transaccion_id",
            nullable = false
    )
    private Transaccion transaccion;

    @PrePersist
    public void prePersist() {

        this.fechaSubida = LocalDateTime.now();

        if (this.estado == null) {

            this.estado =
                    EstadoDocumentoICA.PENDIENTE;
        }
    }

    public enum EstadoDocumentoICA {

        PENDIENTE,
        VALIDADO,
        RECHAZADO
    }
}
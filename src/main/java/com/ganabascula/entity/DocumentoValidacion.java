package com.ganabascula.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "documentos_validacion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoValidacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "tipo_documento", nullable = false)
    private String tipoDocumento = "CERTIFICADO_ICA";

    @Column(name = "archivo_url", nullable = false)
    private String archivoUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoDocumento estado = EstadoDocumento.PENDIENTE;

    @Column(name = "fecha_subida")
    private LocalDateTime fechaSubida = LocalDateTime.now();

    private String observaciones;

    public enum EstadoDocumento {
        PENDIENTE, APROBADO, RECHAZADO
    }
}
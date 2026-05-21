package com.ganabascula.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reportes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    private BigDecimal totalVentas;

    private Integer totalTransacciones;

    private LocalDateTime fechaGeneracion;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @PrePersist
    public void prePersist() {
        this.fechaGeneracion = LocalDateTime.now();
    }
}
package com.ganabascula.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "transacciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Usuario que registra la transacción
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /*
     * Nombre del comprador
     */
    @Column(name = "nombre_comprador", nullable = false)
    private String nombreComprador;

    /*
     * Fecha de la transacción
     */
    @Column(nullable = false)
    private LocalDate fecha;

    /*
     * Estado actual de la transacción
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoTransaccion estado;

    /*
     * Total bruto de la transacción
     */
    @Column(
            name = "total_bruto",
            precision = 15,
            scale = 2,
            nullable = false
    )
    private BigDecimal totalBruto;

    /*
     * Total neto final
     */
    @Column(
            name = "total_neto",
            precision = 15,
            scale = 2,
            nullable = false
    )
    private BigDecimal totalNeto;

    /*
     * Observaciones adicionales
     */
    @Column(length = 500)
    private String observaciones;

    /*
     * Fecha automática de registro
     */
    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    /*
     * Detalles animales de la transacción
     */
    @OneToMany(
            mappedBy = "transaccion",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private List<DetalleTransaccionAnimal> detalles =
            new ArrayList<>();

    /*
     * Gastos adicionales asociados
     */
    @OneToMany(
            mappedBy = "transaccion",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private List<GastoAdicional> gastos =
            new ArrayList<>();

    @PrePersist
    public void prePersist() {

        this.fechaRegistro = LocalDateTime.now();

        if (this.estado == null) {
            this.estado = EstadoTransaccion.BORRADOR;
        }

        if (this.totalBruto == null) {
            this.totalBruto = BigDecimal.ZERO;
        }

        if (this.totalNeto == null) {
            this.totalNeto = BigDecimal.ZERO;
        }
    }

    public enum EstadoTransaccion {

        BORRADOR,
        COMPLETADA,
        ANULADA
    }
}
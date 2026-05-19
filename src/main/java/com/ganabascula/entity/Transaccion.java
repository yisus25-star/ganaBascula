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
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "nombre_comprador", nullable = false)
    private String nombreComprador;

    @Column(nullable = false)
    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoTransaccion estado = EstadoTransaccion.BORRADOR;

    @Column(name = "total_bruto", precision = 15, scale = 2)
    private BigDecimal totalBruto = BigDecimal.ZERO;

    @Column(name = "total_neto", precision = 15, scale = 2)
    private BigDecimal totalNeto = BigDecimal.ZERO;

    private String observaciones;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @OneToMany(mappedBy = "transaccion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoriaAnimal> categorias = new ArrayList<>();

    @OneToMany(mappedBy = "transaccion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GastoAdicional> gastos = new ArrayList<>();

    public enum EstadoTransaccion {
        BORRADOR, COMPLETADA, ANULADA
    }
}
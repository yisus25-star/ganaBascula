package com.ganabascula.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "detalle_transaccion_animal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleTransaccionAnimal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaccion_id", nullable = false)
    private Transaccion transaccion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_animal", nullable = false)
    private TipoAnimal tipo;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(
            name = "peso_bruto_kg",
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal pesoBrutoKg;

    @Column(name = "aplica_destare", nullable = false)
    private Boolean aplicaDestare = false;

    @Column(
            name = "porcentaje_destare",
            precision = 5,
            scale = 2
    )
    private BigDecimal porcentajeDestare = BigDecimal.ZERO;

    @Column(
            name = "peso_neto_kg",
            precision = 10,
            scale = 2
    )
    private BigDecimal pesoNetoKg;

    @Column(
            name = "precio_por_kilo",
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal precioPorKilo;

    @Column(name = "aplica_bono", nullable = false)
    private Boolean aplicaBono = false;

    @Column(
            name = "valor_bono",
            precision = 10,
            scale = 2
    )
    private BigDecimal valorBono = BigDecimal.ZERO;

    @Column(
            name = "subtotal",
            precision = 15,
            scale = 2
    )
    private BigDecimal subtotal = BigDecimal.ZERO;
}
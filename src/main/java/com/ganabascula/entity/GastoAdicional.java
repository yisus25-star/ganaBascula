package com.ganabascula.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "gastos_adicionales")

@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor

public class GastoAdicional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "transaccion_id",
            nullable = false
    )
    private Transaccion transaccion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoGasto tipo;

    @Column(nullable = false)
    private String descripcion;

    @Column(
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal valor;

    @Column(nullable = false)
    private Boolean aplica = true;

    public enum TipoGasto {

        TRANSPORTE,
        VAQUEROS,
        ALIMENTACION,
        GUIAS_SANITARIAS,
        BASCULA,
        COMISION,
        OTROS
    }
}
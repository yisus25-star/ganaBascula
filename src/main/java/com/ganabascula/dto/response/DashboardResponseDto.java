package com.ganabascula.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardResponseDto {

    private Double ingresosTotales;

    private Double gastosTotales;

    private Double utilidadNeta;

    private Long cantidadVentas;

    private Long usuariosRegistrados;

    private Double ventasMes;
}
package com.ganabascula.service.impl;

import com.ganabascula.dto.request.ReporteRequestDto;
import com.ganabascula.dto.response.ReporteResponseDto;
import com.ganabascula.service.ServiceReporte;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceReporteImpl implements ServiceReporte {

    @Override
    public ReporteResponseDto generarReporte(ReporteRequestDto dto, String cedula) {

        // lógica negocio
        // guardar reporte
        // cálculos
        // métricas

        return new ReporteResponseDto();
    }

    @Override
    public List<ReporteResponseDto> listarReportes(String cedula) {

        // lógica listar reportes

        return List.of();
    }
}
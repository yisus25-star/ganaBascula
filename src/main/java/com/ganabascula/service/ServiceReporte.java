package com.ganabascula.service;

import com.ganabascula.dto.request.ReporteRequestDto;
import com.ganabascula.dto.response.ReporteResponseDto;

import java.util.List;

public interface ServiceReporte {

    ReporteResponseDto generarReporte(ReporteRequestDto dto, String cedula);

    List<ReporteResponseDto> listarReportes(String cedula);
}
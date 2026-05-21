package com.ganabascula.service.impl;

import com.ganabascula.dto.request.ReporteRequestDto;
import com.ganabascula.dto.response.ReporteResponseDto;
import com.ganabascula.entity.Reporte;
import com.ganabascula.entity.Usuario;
import com.ganabascula.repository.ReporteRepository;
import com.ganabascula.repository.UsuarioRepository;
import com.ganabascula.service.ServiceReporte;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceReporteImpl implements ServiceReporte {

    private final ReporteRepository reporteRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public ReporteResponseDto generarReporte(
            ReporteRequestDto request,
            String cedulaUsuario
    ) {

        Usuario usuario = usuarioRepository.findByCedula(cedulaUsuario)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));

        Reporte reporte = Reporte.builder()
                .usuario(usuario)
                .fechaInicio(request.getFechaInicio())
                .fechaFin(request.getFechaFin())
                .totalVentas(BigDecimal.ZERO)
                .totalTransacciones(0)
                .build();

        Reporte reporteGuardado = reporteRepository.save(reporte);

        return ReporteResponseDto.builder()
                .id(reporteGuardado.getId())
                .fechaInicio(reporteGuardado.getFechaInicio())
                .fechaFin(reporteGuardado.getFechaFin())
                .totalVentas(reporteGuardado.getTotalVentas())
                .totalTransacciones(
                        reporteGuardado.getTotalTransacciones()
                )
                .build();
    }

    @Override
    public List<ReporteResponseDto> obtenerReportesUsuario(
            String cedulaUsuario
    ) {

        Usuario usuario = usuarioRepository.findByCedula(cedulaUsuario)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));

        return reporteRepository.findByUsuario(usuario)
                .stream()
                .map(reporte -> ReporteResponseDto.builder()
                        .id(reporte.getId())
                        .fechaInicio(reporte.getFechaInicio())
                        .fechaFin(reporte.getFechaFin())
                        .totalVentas(reporte.getTotalVentas())
                        .totalTransacciones(
                                reporte.getTotalTransacciones()
                        )
                        .build())
                .toList();
    }
}
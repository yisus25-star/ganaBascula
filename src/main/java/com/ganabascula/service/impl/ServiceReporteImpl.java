package com.ganabascula.service.impl;

import com.ganabascula.dto.request.ReporteRequestDto;
import com.ganabascula.dto.response.ReporteResponseDto;
import com.ganabascula.entity.Reporte;
import com.ganabascula.entity.Transaccion;
import com.ganabascula.entity.Usuario;
import com.ganabascula.repository.ReporteRepository;
import com.ganabascula.repository.TransaccionRepository;
import com.ganabascula.repository.UsuarioRepository;
import com.ganabascula.service.ServiceReporte;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceReporteImpl implements ServiceReporte {

    private final ReporteRepository reporteRepository;
    private final TransaccionRepository transaccionRepository;
    private final UsuarioRepository usuarioRepository;

    public ServiceReporteImpl(
            ReporteRepository reporteRepository,
            TransaccionRepository transaccionRepository,
            UsuarioRepository usuarioRepository) {
        this.reporteRepository = reporteRepository;
        this.transaccionRepository = transaccionRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public ReporteResponseDto generarReporte(ReporteRequestDto dto, String cedula) {
        Usuario usuario = usuarioRepository.findByCedula(cedula)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Obtener transacciones en el rango de fechas
        List<Transaccion> transacciones = transaccionRepository
                .findByUsuarioAndFechaBetween(usuario, dto.getFechaInicio(), dto.getFechaFin());

        // Filtrar solo completadas
        List<Transaccion> completadas = transacciones.stream()
                .filter(t -> t.getEstado() == Transaccion.EstadoTransaccion.COMPLETADA)
                .collect(Collectors.toList());

        // Calcular totales
        BigDecimal totalVentas = completadas.stream()
                .map(Transaccion::getTotalNeto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalAnimales = completadas.stream()
                .flatMap(t -> t.getCategorias().stream())
                .mapToInt(c -> c.getCantidad())
                .sum();

        // Guardar reporte
        Reporte reporte = new Reporte();
        reporte.setUsuario(usuario);
        reporte.setFechaInicio(dto.getFechaInicio());
        reporte.setFechaFin(dto.getFechaFin());
        reporte.setTotalVentas(totalVentas);
        reporte.setTotalAnimales(totalAnimales);
        reporte.setTotalTransacciones(completadas.size());

        return mapToDto(reporteRepository.save(reporte));
    }

    @Override
    public List<ReporteResponseDto> listarReportes(String cedula) {
        Usuario usuario = usuarioRepository.findByCedula(cedula)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return reporteRepository.findByUsuarioOrderByFechaGeneracionDesc(usuario)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private ReporteResponseDto mapToDto(Reporte reporte) {
        ReporteResponseDto dto = new ReporteResponseDto();
        dto.setId(reporte.getId());
        dto.setFechaGeneracion(reporte.getFechaGeneracion());
        dto.setFechaInicio(reporte.getFechaInicio());
        dto.setFechaFin(reporte.getFechaFin());
        dto.setTotalVentas(reporte.getTotalVentas());
        dto.setTotalAnimales(reporte.getTotalAnimales());
        dto.setTotalTransacciones(reporte.getTotalTransacciones());
        return dto;
    }
}
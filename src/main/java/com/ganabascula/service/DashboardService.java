package com.ganabascula.service;

import com.ganabascula.dto.response.DashboardResponseDto;
import com.ganabascula.repository.GastoAdicionalRepository;
import com.ganabascula.repository.TransaccionRepository;
import com.ganabascula.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TransaccionRepository transaccionRepository;

    private final GastoAdicionalRepository gastoAdicionalRepository;

    private final UsuarioRepository usuarioRepository;

    public DashboardResponseDto obtenerDashboard() {

        Double ingresos =
                transaccionRepository.obtenerIngresosTotales();

        Double gastos =
                gastoAdicionalRepository.obtenerGastosTotales();

        Double utilidad =
                ingresos - gastos;

        Long ventas =
                transaccionRepository.contarVentas();

        Long usuarios =
                usuarioRepository.contarUsuarios();

        Double ventasMes =
                transaccionRepository.obtenerVentasMes();

        return new DashboardResponseDto(
                ingresos,
                gastos,
                utilidad,
                ventas,
                usuarios,
                ventasMes
        );
    }
}
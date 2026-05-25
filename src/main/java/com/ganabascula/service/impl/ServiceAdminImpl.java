package com.ganabascula.service.impl;

import com.ganabascula.dto.response.UsuarioResponseDto;
import com.ganabascula.entity.Transaccion;
import com.ganabascula.entity.Usuario;
import com.ganabascula.repository.TransaccionRepository;
import com.ganabascula.repository.UsuarioRepository;
import com.ganabascula.service.ServiceAdmin;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ServiceAdminImpl implements ServiceAdmin {

    private final UsuarioRepository usuarioRepository;
    private final TransaccionRepository transaccionRepository;

    public ServiceAdminImpl(
            UsuarioRepository usuarioRepository,
            TransaccionRepository transaccionRepository) {
        this.usuarioRepository = usuarioRepository;
        this.transaccionRepository = transaccionRepository;
    }

    @Override
    public List<UsuarioResponseDto> listarUsuarios() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public UsuarioResponseDto cambiarEstado(Long id, String estado) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setEstado(Usuario.EstadoUsuario.valueOf(estado));
        usuarioRepository.save(usuario);

        return UsuarioResponseDto.fromEntity(usuario);
    }

    @Override
    public UsuarioResponseDto obtenerUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return UsuarioResponseDto.fromEntity(usuario);
    }

    @Override
    public Map<String, Object> obtenerMetricas() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<Transaccion> transacciones = transaccionRepository.findAll();

        long activos = usuarios.stream()
                .filter(u -> u.getEstado() == Usuario.EstadoUsuario.ACTIVO)
                .count();

        long pendientes = usuarios.stream()
                .filter(u -> u.getEstado() == Usuario.EstadoUsuario.PENDIENTE)
                .count();

        long rechazados = usuarios.stream()
                .filter(u -> u.getEstado() == Usuario.EstadoUsuario.RECHAZADO)
                .count();

        long totalVentas = transacciones.stream()
                .filter(t -> t.getEstado() == Transaccion.EstadoTransaccion.COMPLETADA)
                .count();

        BigDecimal montoTotal = transacciones.stream()
                .filter(t -> t.getEstado() == Transaccion.EstadoTransaccion.COMPLETADA)
                .map(Transaccion::getTotalNeto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> metricas = new HashMap<>();
        metricas.put("ganaderos_activos", activos);
        metricas.put("ganaderos_pendientes", pendientes);
        metricas.put("ganaderos_rechazados", rechazados);
        metricas.put("total_ventas", totalVentas);
        metricas.put("monto_total_transado", montoTotal);

        return metricas;
    }
}
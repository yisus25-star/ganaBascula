package com.ganabascula.service.impl;

import com.ganabascula.dto.response.MetricasAdminResponseDto;
import com.ganabascula.dto.response.UsuarioResponseDto;
import com.ganabascula.entity.Transaccion;
import com.ganabascula.entity.Usuario;
import com.ganabascula.repository.TransaccionRepository;
import com.ganabascula.repository.UsuarioRepository;
import com.ganabascula.service.ServiceAdmin;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceAdminImpl implements ServiceAdmin {

    private final UsuarioRepository usuarioRepository;

    private final TransaccionRepository transaccionRepository;

    @Override
    public List<UsuarioResponseDto> listarUsuarios() {

        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public UsuarioResponseDto cambiarEstado(
            Long id,
            String estado
    ) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado")
                );

        usuario.setEstado(
                Usuario.EstadoUsuario.valueOf(estado)
        );

        usuarioRepository.save(usuario);

        return UsuarioResponseDto.fromEntity(usuario);
    }

    @Override
    public UsuarioResponseDto obtenerUsuario(Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado")
                );

        return UsuarioResponseDto.fromEntity(usuario);
    }

    @Override
    public MetricasAdminResponseDto obtenerMetricas() {

        List<Usuario> usuarios = usuarioRepository.findAll();

        List<Transaccion> transacciones =
                transaccionRepository.findAll();

        Long activos = usuarios.stream()
                .filter(u ->
                        u.getEstado() ==
                                Usuario.EstadoUsuario.ACTIVO
                )
                .count();

        Long pendientes = usuarios.stream()
                .filter(u ->
                        u.getEstado() ==
                                Usuario.EstadoUsuario.PENDIENTE
                )
                .count();

        Long rechazados = usuarios.stream()
                .filter(u ->
                        u.getEstado() ==
                                Usuario.EstadoUsuario.RECHAZADO
                )
                .count();

        Long totalVentas = transacciones.stream()
                .filter(t ->
                        t.getEstado() ==
                                Transaccion.EstadoTransaccion.COMPLETADA
                )
                .count();

        BigDecimal montoTotal = transacciones.stream()
                .filter(t ->
                        t.getEstado() ==
                                Transaccion.EstadoTransaccion.COMPLETADA
                )
                .map(Transaccion::getTotalNeto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new MetricasAdminResponseDto(
                activos,
                pendientes,
                rechazados,
                totalVentas,
                montoTotal
        );
    }
}
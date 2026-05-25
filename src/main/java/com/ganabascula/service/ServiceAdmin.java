package com.ganabascula.service;

import com.ganabascula.dto.response.MetricasAdminResponseDto;
import com.ganabascula.dto.response.UsuarioResponseDto;

import java.util.List;

public interface ServiceAdmin {

    List<UsuarioResponseDto> listarUsuarios();

    UsuarioResponseDto cambiarEstado(Long id, String estado);

    UsuarioResponseDto obtenerUsuario(Long id);

    MetricasAdminResponseDto obtenerMetricas();
}
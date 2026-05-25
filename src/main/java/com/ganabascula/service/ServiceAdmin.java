package com.ganabascula.service;

import com.ganabascula.dto.response.UsuarioResponseDto;
import java.util.List;
import java.util.Map;

public interface ServiceAdmin {

    List<UsuarioResponseDto> listarUsuarios();

    UsuarioResponseDto cambiarEstado(Long id, String estado);

    UsuarioResponseDto obtenerUsuario(Long id);

    Map<String, Object> obtenerMetricas();
}
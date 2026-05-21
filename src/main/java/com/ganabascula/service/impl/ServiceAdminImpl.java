package com.ganabascula.service.impl;

import com.ganabascula.dto.response.UsuarioResponseDto;
import com.ganabascula.entity.Usuario;
import com.ganabascula.repository.UsuarioRepository;
import com.ganabascula.service.ServiceAdmin;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceAdminImpl implements ServiceAdmin {

    private final UsuarioRepository usuarioRepository;

    public ServiceAdminImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
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
}
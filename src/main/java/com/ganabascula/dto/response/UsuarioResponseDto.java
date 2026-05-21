package com.ganabascula.dto.response;

import com.ganabascula.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponseDto {

    private Long id;
    private String nombre;
    private String cedula;
    private String rol;
    private String estado;
    private String mensaje;

    // Constructor para mensajes simples
    public UsuarioResponseDto(String mensaje) {
        this.mensaje = mensaje;
    }

    // Constructor desde entidad
    public static UsuarioResponseDto fromEntity(Usuario usuario) {
        UsuarioResponseDto dto = new UsuarioResponseDto();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setCedula(usuario.getCedula());
        dto.setRol(usuario.getRol().getNombre());
        dto.setEstado(usuario.getEstado().name());
        return dto;
    }
}
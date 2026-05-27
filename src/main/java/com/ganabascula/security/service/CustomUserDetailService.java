package com.ganabascula.security.service;

import com.ganabascula.entity.Usuario;
import com.ganabascula.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService
        implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(
            String cedula
    ) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository
                .findByCedula(cedula)

                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Usuario no encontrado"
                        )
                );

        // VALIDAR QUE EL USUARIO ESTÉ ACTIVO
        if (
                usuario.getEstado()
                        != Usuario.EstadoUsuario.ACTIVO
        ) {

            throw new UsernameNotFoundException(
                    "Tu cuenta está "
                            + usuario.getEstado()
                            .name()
                            .toLowerCase()
                            + ". Contacta al administrador."
            );
        }

        // RETORNAR USUARIO AUTENTICADO
        return new User(

                usuario.getCedula(),

                usuario.getPassword(),

                List.of(
                        new SimpleGrantedAuthority(
                                usuario.getRol()
                                        .getNombre()
                        )
                )
        );
    }
}
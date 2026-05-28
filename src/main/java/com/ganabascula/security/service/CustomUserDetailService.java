package com.ganabascula.security.service;

import com.ganabascula.entity.Usuario;
import com.ganabascula.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.DisabledException;

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

    private final UsuarioRepository
            usuarioRepository;

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

        if (
                usuario.getEstado()
                        == Usuario.EstadoUsuario.PENDIENTE
        ) {

            throw new DisabledException(
                    "Tu cuenta aún está pendiente de aprobación"
            );
        }

        if (
                usuario.getEstado()
                        == Usuario.EstadoUsuario.RECHAZADO
        ) {

            throw new DisabledException(
                    "Tu cuenta fue rechazada por el administrador"
            );
        }

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
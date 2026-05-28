package com.ganabascula.service.impl;

import com.ganabascula.dto.request.LoginRequestDto;
import com.ganabascula.dto.request.RegistroUsuarioRequestDto;
import com.ganabascula.dto.response.LoginResponseDto;
import com.ganabascula.entity.Rol;
import com.ganabascula.entity.Usuario;
import com.ganabascula.entity.Usuario.EstadoUsuario;
import com.ganabascula.repository.RolRepository;
import com.ganabascula.repository.UsuarioRepository;
import com.ganabascula.security.jwt.JwtService;
import com.ganabascula.service.ServiceUsuario;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ServiceUsuarioImpl implements ServiceUsuario {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public ServiceUsuarioImpl(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager
    ) {

        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void registrarUsuario(
            RegistroUsuarioRequestDto registroDTO
    ) {

        Rol rolGanadero = rolRepository
                .findByNombre("ROLE_GANADERO")
                .orElseThrow(() ->
                        new RuntimeException("Rol no encontrado")
                );

        Usuario usuario = new Usuario();

        usuario.setNombre(registroDTO.getNombre());

        usuario.setCedula(registroDTO.getCedula());

        usuario.setPassword(
                passwordEncoder.encode(
                        registroDTO.getPassword()
                )
        );

        usuario.setRol(rolGanadero);

        usuario.setEstado(EstadoUsuario.PENDIENTE);

        usuarioRepository.save(usuario);
    }

    @Override
    public LoginResponseDto login(
            LoginRequestDto loginDTO
    ) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getCedula(),
                        loginDTO.getPassword()
                )
        );

        Usuario usuario = usuarioRepository
                .findByCedula(loginDTO.getCedula())
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado")
                );

        if (usuario.getEstado() == EstadoUsuario.PENDIENTE) {

            throw new RuntimeException(
                    "Usuario pendiente de aprobación"
            );
        }

        if (usuario.getEstado() == EstadoUsuario.RECHAZADO) {

            throw new RuntimeException(
                    "Usuario rechazado"
            );
        }

        String token = jwtService.generarToken(
                loginDTO.getCedula()
        );

        return new LoginResponseDto(token);
    }

    @Override
    public void aprobarUsuario(Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuario no encontrado"
                        )
                );

        usuario.setEstado(EstadoUsuario.ACTIVO);

        usuarioRepository.save(usuario);
    }

    @Override
    public void rechazarUsuario(Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuario no encontrado"
                        )
                );

        usuario.setEstado(EstadoUsuario.RECHAZADO);

        usuarioRepository.save(usuario);
    }
}
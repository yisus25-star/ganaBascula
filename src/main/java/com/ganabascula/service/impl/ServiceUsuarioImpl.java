package com.ganabascula.service.impl;

import com.ganabascula.dto.request.LoginRequestDto;
import com.ganabascula.dto.request.RegistroUsuarioRequestDto;
import com.ganabascula.dto.response.LoginResponseDto;
import com.ganabascula.entity.Rol;
import com.ganabascula.entity.Usuario;
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
    public void registrarUsuario(RegistroUsuarioRequestDto registroDTO) {

        Rol rolGanadero = rolRepository.findByNombre("ROLE_GANADERO")
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        Usuario usuario = new Usuario();

        usuario.setNombre(registroDTO.getNombre());

        usuario.setCedula(registroDTO.getCedula());

        usuario.setPassword(
                passwordEncoder.encode(registroDTO.getPassword())
        );

        usuario.setRol(rolGanadero);

        usuarioRepository.save(usuario);
    }

    @Override
    public LoginResponseDto login(LoginRequestDto loginDTO) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getCedula(),
                        loginDTO.getPassword()
                )
        );

        String token = jwtService.generarToken(
                loginDTO.getCedula()
        );

        return new LoginResponseDto(token);
    }
}
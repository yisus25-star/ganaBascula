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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ServiceUsuarioImpl implements ServiceUsuario {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public ServiceUsuarioImpl(UsuarioRepository usuarioRepository,
                              RolRepository rolRepository,
                              PasswordEncoder passwordEncoder,
                              JwtService jwtService) {

        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
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

        Usuario usuario = usuarioRepository.findByCedula(loginDTO.getCedula())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        boolean passwordCorrecta = passwordEncoder.matches(
                loginDTO.getPassword(),
                usuario.getPassword()
        );

        if (!passwordCorrecta) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        String token = jwtService.generarToken(usuario.getCedula());

        return new LoginResponseDto(token);
    }

}
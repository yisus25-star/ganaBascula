package com.ganabascula.controller;

import com.ganabascula.dto.request.LoginRequestDto;
import com.ganabascula.dto.request.RegistroUsuarioRequestDto;
import com.ganabascula.dto.response.LoginResponseDto;
import com.ganabascula.dto.response.UsuarioResponseDto;
import com.ganabascula.service.ServiceUsuario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final ServiceUsuario serviceUsuario;

    public UsuarioController(ServiceUsuario serviceUsuario) {
        this.serviceUsuario = serviceUsuario;
    }

    @PostMapping("/registro")
    public ResponseEntity<UsuarioResponseDto> registrarUsuario(
            @RequestBody RegistroUsuarioRequestDto registroDTO
    ) {

        serviceUsuario.registrarUsuario(registroDTO);

        UsuarioResponseDto response =
                new UsuarioResponseDto("Usuario registrado correctamente");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @RequestBody LoginRequestDto loginDTO
    ) {

        LoginResponseDto response = serviceUsuario.login(loginDTO);

        return ResponseEntity.ok(response);
    }

}
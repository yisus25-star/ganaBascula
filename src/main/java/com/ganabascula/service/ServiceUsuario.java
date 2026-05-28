package com.ganabascula.service;

import com.ganabascula.dto.request.LoginRequestDto;
import com.ganabascula.dto.request.RegistroUsuarioRequestDto;
import com.ganabascula.dto.response.LoginResponseDto;

public interface ServiceUsuario {

    void registrarUsuario(
            RegistroUsuarioRequestDto registroDTO
    );

    LoginResponseDto login(
            LoginRequestDto loginDTO
    );

    void aprobarUsuario(Long id);

    void rechazarUsuario(Long id);
}
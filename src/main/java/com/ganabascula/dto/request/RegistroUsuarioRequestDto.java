package com.ganabascula.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroUsuarioRequestDto {

    private String nombre;

    private String cedula;

    private String password;

}

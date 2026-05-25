package com.ganabascula.controller;

import com.ganabascula.dto.request.CambiarEstadoRequestDto;

import com.ganabascula.dto.response.ApiResponse;
import com.ganabascula.dto.response.MetricasAdminResponseDto;
import com.ganabascula.dto.response.UsuarioResponseDto;

import com.ganabascula.service.ServiceAdmin;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final ServiceAdmin serviceAdmin;

    @GetMapping("/usuarios")
    public ResponseEntity<ApiResponse<List<UsuarioResponseDto>>>
    listarUsuarios() {

        return ResponseEntity.ok(

                new ApiResponse<>(

                        "Usuarios obtenidos correctamente",

                        serviceAdmin.listarUsuarios()
                )
        );
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<ApiResponse<UsuarioResponseDto>>
    obtenerUsuario(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(

                new ApiResponse<>(

                        "Usuario obtenido correctamente",

                        serviceAdmin.obtenerUsuario(id)
                )
        );
    }

    @PatchMapping("/usuarios/{id}/estado")
    public ResponseEntity<ApiResponse<UsuarioResponseDto>>
    cambiarEstado(
            @PathVariable Long id,

            @RequestBody CambiarEstadoRequestDto dto
    ) {

        return ResponseEntity.ok(

                new ApiResponse<>(

                        "Estado actualizado correctamente",

                        serviceAdmin.cambiarEstado(
                                id,
                                dto.getEstado()
                        )
                )
        );
    }

    @GetMapping("/metricas")
    public ResponseEntity<ApiResponse<MetricasAdminResponseDto>>
    obtenerMetricas() {

        return ResponseEntity.ok(

                new ApiResponse<>(

                        "Métricas obtenidas correctamente",

                        serviceAdmin.obtenerMetricas()
                )
        );
    }
}
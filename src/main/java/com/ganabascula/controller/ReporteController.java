package com.ganabascula.controller;

import com.ganabascula.dto.request.ReporteRequestDto;

import com.ganabascula.dto.response.ApiResponse;
import com.ganabascula.dto.response.ReporteResponseDto;

import com.ganabascula.service.ServiceReporte;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ServiceReporte serviceReporte;

    @PostMapping
    public ResponseEntity<ApiResponse<ReporteResponseDto>>
    generarReporte(

            @Valid @RequestBody ReporteRequestDto dto,

            Authentication auth
    ) {

        ReporteResponseDto response =

                serviceReporte.generarReporte(
                        dto,
                        auth.getName()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)

                .body(

                        new ApiResponse<>(

                                "Reporte generado correctamente",

                                response
                        )
                );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReporteResponseDto>>>
    listarReportes(
            Authentication auth
    ) {

        List<ReporteResponseDto> response =

                serviceReporte.listarReportes(
                        auth.getName()
                );

        return ResponseEntity.ok(

                new ApiResponse<>(

                        "Reportes obtenidos correctamente",

                        response
                )
        );
    }
}
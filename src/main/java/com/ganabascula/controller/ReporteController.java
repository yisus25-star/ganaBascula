package com.ganabascula.controller;

import com.ganabascula.dto.request.ReporteRequestDto;
import com.ganabascula.dto.response.ReporteResponseDto;
import com.ganabascula.service.ServiceReporte;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reportes")
public class ReporteController {

    private final ServiceReporte serviceReporte;

    public ReporteController(ServiceReporte serviceReporte) {
        this.serviceReporte = serviceReporte;
    }

    @PostMapping
    public ResponseEntity<ReporteResponseDto> generarReporte(
            @Valid @RequestBody ReporteRequestDto dto,
            Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(serviceReporte.generarReporte(dto, auth.getName()));
    }

    @GetMapping
    public ResponseEntity<List<ReporteResponseDto>> listarReportes(
            Authentication auth) {
        return ResponseEntity.ok(serviceReporte.listarReportes(auth.getName()));
    }
}
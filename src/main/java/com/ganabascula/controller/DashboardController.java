package com.ganabascula.controller;

import com.ganabascula.dto.response.ApiResponse;
import com.ganabascula.dto.response.DashboardResponseDto;
import com.ganabascula.service.DashboardService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<ApiResponse<DashboardResponseDto>> obtenerDashboard() {

        return ResponseEntity.ok(

                new ApiResponse<>(

                        "Dashboard obtenido correctamente",

                        dashboardService.obtenerDashboard()
                )
        );
    }
}
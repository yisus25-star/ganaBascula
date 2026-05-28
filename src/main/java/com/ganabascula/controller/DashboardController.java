package com.ganabascula.controller;

import com.ganabascula.dto.response.DashboardResponseDto;
import com.ganabascula.service.DashboardService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<DashboardResponseDto> obtenerDashboard() {

        return ResponseEntity.ok(
                dashboardService.obtenerDashboard()
        );
    }
}
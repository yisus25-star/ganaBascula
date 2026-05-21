package com.ganabascula.controller;

import com.ganabascula.dto.response.UsuarioResponseDto;
import com.ganabascula.service.ServiceAdmin;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final ServiceAdmin serviceAdmin;

    public AdminController(ServiceAdmin serviceAdmin) {
        this.serviceAdmin = serviceAdmin;
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioResponseDto>> listarUsuarios() {
        return ResponseEntity.ok(serviceAdmin.listarUsuarios());
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<UsuarioResponseDto> obtenerUsuario(
            @PathVariable Long id) {
        return ResponseEntity.ok(serviceAdmin.obtenerUsuario(id));
    }

    @PatchMapping("/usuarios/{id}/estado")
    public ResponseEntity<UsuarioResponseDto> cambiarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(
                serviceAdmin.cambiarEstado(id, body.get("estado")));
    }
}
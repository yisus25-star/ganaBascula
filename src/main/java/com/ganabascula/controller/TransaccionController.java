package com.ganabascula.controller;

import com.ganabascula.dto.request.CategoriaAnimalRequestDto;
import com.ganabascula.dto.request.GastoAdicionalRequestDto;
import com.ganabascula.dto.request.TransaccionRequestDto;
import com.ganabascula.dto.response.TransaccionResponseDto;
import com.ganabascula.service.ServiceTransaccion;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transacciones")
public class TransaccionController {

    private final ServiceTransaccion serviceTransaccion;

    public TransaccionController(ServiceTransaccion serviceTransaccion) {
        this.serviceTransaccion = serviceTransaccion;
    }

    @PostMapping
    public ResponseEntity<TransaccionResponseDto> crear(
            @Valid @RequestBody TransaccionRequestDto dto,
            Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(serviceTransaccion.crearTransaccion(dto, auth.getName()));
    }

    @GetMapping
    public ResponseEntity<List<TransaccionResponseDto>> listar(Authentication auth) {
        return ResponseEntity.ok(serviceTransaccion.listarTransacciones(auth.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransaccionResponseDto> obtener(
            @PathVariable Long id,
            Authentication auth) {
        return ResponseEntity.ok(serviceTransaccion.obtenerTransaccion(id, auth.getName()));
    }

    @PostMapping("/{id}/categorias")
    public ResponseEntity<TransaccionResponseDto> agregarCategoria(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaAnimalRequestDto dto,
            Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(serviceTransaccion.agregarCategoria(id, dto, auth.getName()));
    }

    @PostMapping("/{id}/gastos")
    public ResponseEntity<TransaccionResponseDto> agregarGasto(
            @PathVariable Long id,
            @Valid @RequestBody GastoAdicionalRequestDto dto,
            Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(serviceTransaccion.agregarGasto(id, dto, auth.getName()));
    }

    @PatchMapping("/{id}/completar")
    public ResponseEntity<TransaccionResponseDto> completar(
            @PathVariable Long id,
            Authentication auth) {
        return ResponseEntity.ok(serviceTransaccion.completarTransaccion(id, auth.getName()));
    }

    @GetMapping("/filtrar/fecha")
    public ResponseEntity<List<TransaccionResponseDto>> filtrarPorFecha(
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin,
            Authentication auth) {
        return ResponseEntity.ok(
                serviceTransaccion.filtrarPorFecha(auth.getName(), fechaInicio, fechaFin));
    }

    @GetMapping("/filtrar/comprador")
    public ResponseEntity<List<TransaccionResponseDto>> filtrarPorComprador(
            @RequestParam String nombre,
            Authentication auth) {
        return ResponseEntity.ok(
                serviceTransaccion.filtrarPorComprador(auth.getName(), nombre));
    }
}
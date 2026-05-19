package com.ganabascula.service;

import com.ganabascula.dto.request.CategoriaAnimalRequestDto;
import com.ganabascula.dto.request.GastoAdicionalRequestDto;
import com.ganabascula.dto.request.TransaccionRequestDto;
import com.ganabascula.dto.response.TransaccionResponseDto;
import java.util.List;

public interface ServiceTransaccion {

    TransaccionResponseDto crearTransaccion(TransaccionRequestDto dto, String cedula);

    TransaccionResponseDto agregarCategoria(Long transaccionId, CategoriaAnimalRequestDto dto, String cedula);

    TransaccionResponseDto agregarGasto(Long transaccionId, GastoAdicionalRequestDto dto, String cedula);

    TransaccionResponseDto completarTransaccion(Long transaccionId, String cedula);

    TransaccionResponseDto obtenerTransaccion(Long transaccionId, String cedula);

    List<TransaccionResponseDto> listarTransacciones(String cedula);

    List<TransaccionResponseDto> filtrarPorFecha(String cedula, String fechaInicio, String fechaFin);

    List<TransaccionResponseDto> filtrarPorComprador(String cedula, String nombreComprador);
}
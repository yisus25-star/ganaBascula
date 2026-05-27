package com.ganabascula.service.impl;

import com.ganabascula.dto.request.CategoriaAnimalRequestDto;
import com.ganabascula.dto.request.GastoAdicionalRequestDto;
import com.ganabascula.dto.request.TransaccionRequestDto;
import com.ganabascula.dto.response.TransaccionResponseDto;

import com.ganabascula.entity.DetalleTransaccionAnimal;
import com.ganabascula.entity.GastoAdicional;
import com.ganabascula.entity.Transaccion;
import com.ganabascula.entity.Usuario;

import com.ganabascula.entity.Transaccion.EstadoTransaccion;

import com.ganabascula.mapper.TransaccionMapper;

import com.ganabascula.repository.DetalleTransaccionAnimalRepository;
import com.ganabascula.repository.GastoAdicionalRepository;
import com.ganabascula.repository.TransaccionRepository;
import com.ganabascula.repository.UsuarioRepository;

import com.ganabascula.service.CalculoGanaderoService;
import com.ganabascula.service.ServiceTransaccion;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceTransaccionImpl
        implements ServiceTransaccion {

    private final TransaccionRepository
            transaccionRepository;

    private final DetalleTransaccionAnimalRepository
            detalleTransaccionAnimalRepository;

    private final GastoAdicionalRepository
            gastoAdicionalRepository;

    private final UsuarioRepository
            usuarioRepository;

    private final TransaccionMapper
            transaccionMapper;

    private final CalculoGanaderoService
            calculoGanaderoService;

    public ServiceTransaccionImpl(

            TransaccionRepository
                    transaccionRepository,

            DetalleTransaccionAnimalRepository
                    detalleTransaccionAnimalRepository,

            GastoAdicionalRepository
                    gastoAdicionalRepository,

            UsuarioRepository
                    usuarioRepository,

            TransaccionMapper
                    transaccionMapper,

            CalculoGanaderoService
                    calculoGanaderoService
    ) {

        this.transaccionRepository =
                transaccionRepository;

        this.detalleTransaccionAnimalRepository =
                detalleTransaccionAnimalRepository;

        this.gastoAdicionalRepository =
                gastoAdicionalRepository;

        this.usuarioRepository =
                usuarioRepository;

        this.transaccionMapper =
                transaccionMapper;

        this.calculoGanaderoService =
                calculoGanaderoService;
    }

    @Override
    @Transactional
    public TransaccionResponseDto crearTransaccion(
            TransaccionRequestDto dto,
            String cedula
    ) {

        Usuario usuario = usuarioRepository
                .findByCedula(cedula)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuario no encontrado"
                        )
                );

        Transaccion transaccion =
                transaccionMapper.toEntity(dto);

        transaccion.setUsuario(usuario);

        transaccion.setEstado(
                EstadoTransaccion.BORRADOR
        );

        Transaccion transaccionGuardada =
                transaccionRepository.save(
                        transaccion
                );

        return transaccionMapper.toDto(
                transaccionGuardada
        );
    }

    @Override
    @Transactional
    public TransaccionResponseDto agregarCategoria(
            Long transaccionId,
            CategoriaAnimalRequestDto dto,
            String cedula
    ) {

        Transaccion transaccion =
                obtenerTransaccionValidada(
                        transaccionId,
                        cedula
                );

        validarTransaccionEditable(
                transaccion
        );

        BigDecimal porcentajeDestare =
                calculoGanaderoService
                        .obtenerPorcentajeDestare(
                                dto.getAplicaDestare(),
                                dto.getTipo()
                        );

        BigDecimal pesoNeto =
                calculoGanaderoService
                        .calcularPesoNeto(
                                dto.getPesoBrutoKg(),
                                porcentajeDestare
                        );

        BigDecimal valorBono =
                calculoGanaderoService
                        .calcularBono(
                                dto.getAplicaBono(),
                                dto.getCantidad()
                        );

        BigDecimal subtotal =
                calculoGanaderoService
                        .calcularSubtotal(
                                pesoNeto,
                                dto.getPrecioPorKilo(),
                                valorBono
                        );

        DetalleTransaccionAnimal detalle =
                new DetalleTransaccionAnimal();

        detalle.setTransaccion(
                transaccion
        );

        detalle.setTipo(
                dto.getTipo()
        );

        detalle.setCantidad(
                dto.getCantidad()
        );

        detalle.setPesoBrutoKg(
                dto.getPesoBrutoKg()
        );

        detalle.setAplicaDestare(
                dto.getAplicaDestare()
        );

        detalle.setPorcentajeDestare(
                porcentajeDestare
        );

        detalle.setPesoNetoKg(
                pesoNeto
        );

        detalle.setPrecioPorKilo(
                dto.getPrecioPorKilo()
        );

        detalle.setAplicaBono(
                dto.getAplicaBono()
        );

        detalle.setValorBono(
                valorBono
        );

        detalle.setSubtotal(
                subtotal
        );

        transaccion.getDetalles()
                .add(detalle);

        detalleTransaccionAnimalRepository
                .save(detalle);

        recalcularTotales(
                transaccion
        );

        transaccionRepository.save(
                transaccion
        );

        Transaccion transaccionActualizada =
                transaccionRepository.findById(
                        transaccion.getId()
                ).orElseThrow();

        return transaccionMapper.toDto(
                transaccionActualizada
        );
    }

    @Override
    @Transactional
    public TransaccionResponseDto agregarGasto(
            Long transaccionId,
            GastoAdicionalRequestDto dto,
            String cedula
    ) {

        Transaccion transaccion =
                obtenerTransaccionValidada(
                        transaccionId,
                        cedula
                );

        validarTransaccionEditable(
                transaccion
        );

        GastoAdicional gasto =
                new GastoAdicional();

        gasto.setTransaccion(
                transaccion
        );

        gasto.setTipo(
                dto.getTipo()
        );

        gasto.setDescripcion(
                dto.getDescripcion()
        );

        gasto.setValor(
                dto.getValor()
        );

        gasto.setAplica(
                dto.getAplica()
        );

        transaccion.getGastos()
                .add(gasto);

        gastoAdicionalRepository.save(
                gasto
        );

        recalcularTotales(
                transaccion
        );

        transaccionRepository.save(
                transaccion
        );

        Transaccion transaccionActualizada =
                transaccionRepository.findById(
                        transaccion.getId()
                ).orElseThrow();

        return transaccionMapper.toDto(
                transaccionActualizada
        );
    }

    @Override
    @Transactional
    public TransaccionResponseDto completarTransaccion(
            Long transaccionId,
            String cedula
    ) {

        Transaccion transaccion =
                obtenerTransaccionValidada(
                        transaccionId,
                        cedula
                );

        if (
                transaccion.getDetalles()
                        .isEmpty()
        ) {

            throw new RuntimeException(
                    "La transaccion debe tener al menos un detalle animal"
            );
        }

        transaccion.setEstado(
                EstadoTransaccion.COMPLETADA
        );

        Transaccion transaccionCompletada =
                transaccionRepository.save(
                        transaccion
                );

        return transaccionMapper.toDto(
                transaccionCompletada
        );
    }

    @Override
    public TransaccionResponseDto obtenerTransaccion(
            Long transaccionId,
            String cedula
    ) {

        Transaccion transaccion =
                obtenerTransaccionValidada(
                        transaccionId,
                        cedula
                );

        return transaccionMapper.toDto(
                transaccion
        );
    }

    @Override
    public List<TransaccionResponseDto>
    listarTransacciones(
            String cedula
    ) {

        Usuario usuario = usuarioRepository
                .findByCedula(cedula)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuario no encontrado"
                        )
                );

        return transaccionRepository
                .findByUsuario(usuario)
                .stream()
                .map(transaccionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransaccionResponseDto>
    filtrarPorFecha(
            String cedula,
            String fechaInicio,
            String fechaFin
    ) {

        Usuario usuario = usuarioRepository
                .findByCedula(cedula)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuario no encontrado"
                        )
                );

        return transaccionRepository
                .findByUsuarioAndFechaBetween(
                        usuario,
                        LocalDate.parse(fechaInicio),
                        LocalDate.parse(fechaFin)
                )
                .stream()
                .map(transaccionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransaccionResponseDto>
    filtrarPorComprador(
            String cedula,
            String nombreComprador
    ) {

        Usuario usuario = usuarioRepository
                .findByCedula(cedula)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuario no encontrado"
                        )
                );

        return transaccionRepository
                .findByUsuarioAndNombreCompradorContainingIgnoreCase(
                        usuario,
                        nombreComprador
                )
                .stream()
                .map(transaccionMapper::toDto)
                .collect(Collectors.toList());
    }

    private void recalcularTotales(
            Transaccion transaccion
    ) {

        BigDecimal totalBruto =
                transaccion.getDetalles()
                        .stream()
                        .map(
                                DetalleTransaccionAnimal
                                        ::getSubtotal
                        )
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        BigDecimal totalGastos =
                transaccion.getGastos()
                        .stream()
                        .filter(
                                GastoAdicional
                                        ::getAplica
                        )
                        .map(
                                GastoAdicional
                                        ::getValor
                        )
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        transaccion.setTotalBruto(
                totalBruto
        );

        transaccion.setTotalNeto(
                totalBruto.subtract(totalGastos)
        );
    }

    private Transaccion obtenerTransaccionValidada(
            Long transaccionId,
            String cedula
    ) {

        Transaccion transaccion =
                transaccionRepository.findById(
                        transaccionId
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Transaccion no encontrada"
                        )
                );

        if (
                !transaccion.getUsuario()
                        .getCedula()
                        .equals(cedula)
        ) {

            throw new RuntimeException(
                    "No tienes permiso para acceder a esta transaccion"
            );
        }

        return transaccion;
    }

    private void validarTransaccionEditable(
            Transaccion transaccion
    ) {

        if (
                transaccion.getEstado()
                        == EstadoTransaccion.COMPLETADA
        ) {

            throw new RuntimeException(
                    "No se puede modificar una transaccion completada"
            );
        }
    }
}
package com.ganabascula.service.impl;

import com.ganabascula.dto.request.CategoriaAnimalRequestDto;
import com.ganabascula.dto.request.GastoAdicionalRequestDto;
import com.ganabascula.dto.request.TransaccionRequestDto;
import com.ganabascula.dto.response.TransaccionResponseDto;
import com.ganabascula.entity.*;
import com.ganabascula.entity.CategoriaAnimal.TipoAnimal;
import com.ganabascula.entity.Transaccion.EstadoTransaccion;
import com.ganabascula.mapper.TransaccionMapper;
import com.ganabascula.repository.*;
import com.ganabascula.service.ServiceTransaccion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceTransaccionImpl implements ServiceTransaccion {

    private static final BigDecimal BONO_POR_ANIMAL = new BigDecimal("42800");
    private static final BigDecimal DESTARE_MACHOS = new BigDecimal("0.05");
    private static final BigDecimal DESTARE_HEMBRAS = new BigDecimal("0.06");

    private final TransaccionRepository transaccionRepository;
    private final CategoriaAnimalRepository categoriaAnimalRepository;
    private final GastoAdicionalRepository gastoAdicionalRepository;
    private final UsuarioRepository usuarioRepository;
    private final TransaccionMapper transaccionMapper;

    public ServiceTransaccionImpl(
            TransaccionRepository transaccionRepository,
            CategoriaAnimalRepository categoriaAnimalRepository,
            GastoAdicionalRepository gastoAdicionalRepository,
            UsuarioRepository usuarioRepository,
            TransaccionMapper transaccionMapper) {
        this.transaccionRepository = transaccionRepository;
        this.categoriaAnimalRepository = categoriaAnimalRepository;
        this.gastoAdicionalRepository = gastoAdicionalRepository;
        this.usuarioRepository = usuarioRepository;
        this.transaccionMapper = transaccionMapper;
    }

    @Override
    @Transactional
    public TransaccionResponseDto crearTransaccion(TransaccionRequestDto dto, String cedula) {
        Usuario usuario = usuarioRepository.findByCedula(cedula)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Transaccion transaccion = transaccionMapper.toEntity(dto);
        transaccion.setUsuario(usuario);
        transaccion.setEstado(EstadoTransaccion.BORRADOR);

        return transaccionMapper.toDto(transaccionRepository.save(transaccion));
    }

    @Override
    @Transactional
    public TransaccionResponseDto agregarCategoria(Long transaccionId, CategoriaAnimalRequestDto dto, String cedula) {
        Transaccion transaccion = obtenerTransaccionValidada(transaccionId, cedula);

        if (transaccion.getEstado() == EstadoTransaccion.COMPLETADA) {
            throw new RuntimeException("No se puede modificar una transaccion completada");
        }

        CategoriaAnimal categoria = new CategoriaAnimal();
        categoria.setTransaccion(transaccion);
        categoria.setTipo(dto.getTipo());
        categoria.setCantidad(dto.getCantidad());
        categoria.setPesoBrutoKg(dto.getPesoBrutoKg());
        categoria.setPrecioPorKilo(dto.getPrecioPorKilo());
        categoria.setAplicaDestare(dto.getAplicaDestare());
        categoria.setAplicaBono(dto.getAplicaBono());

        // Calcular destare
        BigDecimal porcentajeDestare = BigDecimal.ZERO;
        if (dto.getAplicaDestare()) {
            porcentajeDestare = esMacho(dto.getTipo()) ? DESTARE_MACHOS : DESTARE_HEMBRAS;
        }
        categoria.setPorcentajeDestare(porcentajeDestare);

        // Calcular peso neto
        BigDecimal pesoNeto = dto.getPesoBrutoKg()
                .subtract(dto.getPesoBrutoKg().multiply(porcentajeDestare))
                .setScale(2, RoundingMode.HALF_UP);
        categoria.setPesoNetoKg(pesoNeto);

        // Calcular bono
        BigDecimal valorBono = BigDecimal.ZERO;
        if (dto.getAplicaBono()) {
            valorBono = BONO_POR_ANIMAL.multiply(new BigDecimal(dto.getCantidad()));
        }
        categoria.setValorBono(valorBono);

        // Calcular valor categoria
        BigDecimal valorCategoria = pesoNeto
                .multiply(dto.getPrecioPorKilo())
                .subtract(valorBono)
                .setScale(2, RoundingMode.HALF_UP);
        categoria.setValorCategoria(valorCategoria);

        categoriaAnimalRepository.save(categoria);
        recalcularTotales(transaccion);

        return transaccionMapper.toDto(transaccionRepository.save(transaccion));
    }

    @Override
    @Transactional
    public TransaccionResponseDto agregarGasto(Long transaccionId, GastoAdicionalRequestDto dto, String cedula) {
        Transaccion transaccion = obtenerTransaccionValidada(transaccionId, cedula);

        if (transaccion.getEstado() == EstadoTransaccion.COMPLETADA) {
            throw new RuntimeException("No se puede modificar una transaccion completada");
        }

        GastoAdicional gasto = new GastoAdicional();
        gasto.setTransaccion(transaccion);
        gasto.setTipo(dto.getTipo());
        gasto.setDescripcion(dto.getDescripcion());
        gasto.setValor(dto.getValor());
        gasto.setAplica(true);

        gastoAdicionalRepository.save(gasto);
        recalcularTotales(transaccion);

        return transaccionMapper.toDto(transaccionRepository.save(transaccion));
    }

    @Override
    @Transactional
    public TransaccionResponseDto completarTransaccion(Long transaccionId, String cedula) {
        Transaccion transaccion = obtenerTransaccionValidada(transaccionId, cedula);

        if (transaccion.getCategorias().isEmpty()) {
            throw new RuntimeException("La transaccion debe tener al menos una categoria");
        }

        transaccion.setEstado(EstadoTransaccion.COMPLETADA);
        return transaccionMapper.toDto(transaccionRepository.save(transaccion));
    }

    @Override
    public TransaccionResponseDto obtenerTransaccion(Long transaccionId, String cedula) {
        return transaccionMapper.toDto(obtenerTransaccionValidada(transaccionId, cedula));
    }

    @Override
    public List<TransaccionResponseDto> listarTransacciones(String cedula) {
        Usuario usuario = usuarioRepository.findByCedula(cedula)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return transaccionRepository.findByUsuario(usuario)
                .stream().map(transaccionMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<TransaccionResponseDto> filtrarPorFecha(String cedula, String fechaInicio, String fechaFin) {
        Usuario usuario = usuarioRepository.findByCedula(cedula)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return transaccionRepository.findByUsuarioAndFechaBetween(
                        usuario,
                        LocalDate.parse(fechaInicio),
                        LocalDate.parse(fechaFin))
                .stream().map(transaccionMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<TransaccionResponseDto> filtrarPorComprador(String cedula, String nombreComprador) {
        Usuario usuario = usuarioRepository.findByCedula(cedula)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return transaccionRepository.findByUsuarioAndNombreCompradorContainingIgnoreCase(
                        usuario, nombreComprador)
                .stream().map(transaccionMapper::toDto).collect(Collectors.toList());
    }

    private boolean esMacho(TipoAnimal tipo) {
        return tipo == TipoAnimal.BECERRO
                || tipo == TipoAnimal.NOVILLO
                || tipo == TipoAnimal.TORO;
    }

    private void recalcularTotales(Transaccion transaccion) {
        BigDecimal totalBruto = transaccion.getCategorias().stream()
                .map(CategoriaAnimal::getValorCategoria)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalGastos = transaccion.getGastos().stream()
                .filter(GastoAdicional::getAplica)
                .map(GastoAdicional::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        transaccion.setTotalBruto(totalBruto);
        transaccion.setTotalNeto(totalBruto.subtract(totalGastos));
    }

    private Transaccion obtenerTransaccionValidada(Long transaccionId, String cedula) {
        Transaccion transaccion = transaccionRepository.findById(transaccionId)
                .orElseThrow(() -> new RuntimeException("Transaccion no encontrada"));

        if (!transaccion.getUsuario().getCedula().equals(cedula)) {
            throw new RuntimeException("No tienes permiso para acceder a esta transaccion");
        }

        return transaccion;
    }
}
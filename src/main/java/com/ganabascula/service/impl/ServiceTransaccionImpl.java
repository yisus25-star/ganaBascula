package com.ganabascula.service.impl;

import com.ganabascula.dto.request.CategoriaAnimalRequestDto;
import com.ganabascula.dto.request.GastoAdicionalRequestDto;
import com.ganabascula.dto.request.TransaccionRequestDto;
import com.ganabascula.dto.response.CategoriaAnimalResponseDto;
import com.ganabascula.dto.response.GastoAdicionalResponseDto;
import com.ganabascula.dto.response.TransaccionResponseDto;
import com.ganabascula.entity.*;
import com.ganabascula.entity.CategoriaAnimal.TipoAnimal;
import com.ganabascula.entity.Transaccion.EstadoTransaccion;
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

    public ServiceTransaccionImpl(
            TransaccionRepository transaccionRepository,
            CategoriaAnimalRepository categoriaAnimalRepository,
            GastoAdicionalRepository gastoAdicionalRepository,
            UsuarioRepository usuarioRepository) {
        this.transaccionRepository = transaccionRepository;
        this.categoriaAnimalRepository = categoriaAnimalRepository;
        this.gastoAdicionalRepository = gastoAdicionalRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional
    public TransaccionResponseDto crearTransaccion(TransaccionRequestDto dto, String cedula) {
        Usuario usuario = usuarioRepository.findByCedula(cedula)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Transaccion transaccion = new Transaccion();
        transaccion.setUsuario(usuario);
        transaccion.setNombreComprador(dto.getNombreComprador());
        transaccion.setFecha(dto.getFecha());
        transaccion.setObservaciones(dto.getObservaciones());
        transaccion.setEstado(EstadoTransaccion.BORRADOR);

        return mapToDto(transaccionRepository.save(transaccion));
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

        // Recalcular totales
        recalcularTotales(transaccion);

        return mapToDto(transaccionRepository.save(transaccion));
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

        return mapToDto(transaccionRepository.save(transaccion));
    }

    @Override
    @Transactional
    public TransaccionResponseDto completarTransaccion(Long transaccionId, String cedula) {
        Transaccion transaccion = obtenerTransaccionValidada(transaccionId, cedula);

        if (transaccion.getCategorias().isEmpty()) {
            throw new RuntimeException("La transaccion debe tener al menos una categoria de animales");
        }

        transaccion.setEstado(EstadoTransaccion.COMPLETADA);

        return mapToDto(transaccionRepository.save(transaccion));
    }

    @Override
    public TransaccionResponseDto obtenerTransaccion(Long transaccionId, String cedula) {
        return mapToDto(obtenerTransaccionValidada(transaccionId, cedula));
    }

    @Override
    public List<TransaccionResponseDto> listarTransacciones(String cedula) {
        Usuario usuario = usuarioRepository.findByCedula(cedula)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return transaccionRepository.findByUsuario(usuario)
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<TransaccionResponseDto> filtrarPorFecha(String cedula, String fechaInicio, String fechaFin) {
        Usuario usuario = usuarioRepository.findByCedula(cedula)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return transaccionRepository.findByUsuarioAndFechaBetween(
                        usuario,
                        LocalDate.parse(fechaInicio),
                        LocalDate.parse(fechaFin))
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<TransaccionResponseDto> filtrarPorComprador(String cedula, String nombreComprador) {
        Usuario usuario = usuarioRepository.findByCedula(cedula)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return transaccionRepository.findByUsuarioAndNombreCompradorContainingIgnoreCase(
                        usuario, nombreComprador)
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    // MÉTODOS PRIVADOS

    private boolean esMacho(TipoAnimal tipo) {
        return tipo == TipoAnimal.BECERRO
                || tipo == TipoAnimal.NOVILLO
                || tipo == TipoAnimal.TORO;
    }

    private void recalcularTotales(Transaccion transaccion) {
        // Total bruto = suma de todas las categorías
        BigDecimal totalBruto = transaccion.getCategorias().stream()
                .map(CategoriaAnimal::getValorCategoria)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Total gastos
        BigDecimal totalGastos = transaccion.getGastos().stream()
                .filter(GastoAdicional::getAplica)
                .map(GastoAdicional::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Total neto = total bruto - gastos
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

    private TransaccionResponseDto mapToDto(Transaccion transaccion) {
        TransaccionResponseDto dto = new TransaccionResponseDto();
        dto.setId(transaccion.getId());
        dto.setNombreComprador(transaccion.getNombreComprador());
        dto.setFecha(transaccion.getFecha());
        dto.setEstado(transaccion.getEstado());
        dto.setTotalBruto(transaccion.getTotalBruto());
        dto.setTotalNeto(transaccion.getTotalNeto());
        dto.setObservaciones(transaccion.getObservaciones());
        dto.setFechaRegistro(transaccion.getFechaRegistro());

        dto.setCategorias(transaccion.getCategorias().stream()
                .map(this::mapCategoriaToDto)
                .collect(Collectors.toList()));

        dto.setGastos(transaccion.getGastos().stream()
                .map(this::mapGastoToDto)
                .collect(Collectors.toList()));

        return dto;
    }

    private CategoriaAnimalResponseDto mapCategoriaToDto(CategoriaAnimal categoria) {
        CategoriaAnimalResponseDto dto = new CategoriaAnimalResponseDto();
        dto.setId(categoria.getId());
        dto.setTipo(categoria.getTipo());
        dto.setCantidad(categoria.getCantidad());
        dto.setPesoBrutoKg(categoria.getPesoBrutoKg());
        dto.setAplicaDestare(categoria.getAplicaDestare());
        dto.setPorcentajeDestare(categoria.getPorcentajeDestare());
        dto.setPesoNetoKg(categoria.getPesoNetoKg());
        dto.setPrecioPorKilo(categoria.getPrecioPorKilo());
        dto.setAplicaBono(categoria.getAplicaBono());
        dto.setValorBono(categoria.getValorBono());
        dto.setValorCategoria(categoria.getValorCategoria());
        return dto;
    }

    private GastoAdicionalResponseDto mapGastoToDto(GastoAdicional gasto) {
        GastoAdicionalResponseDto dto = new GastoAdicionalResponseDto();
        dto.setId(gasto.getId());
        dto.setTipo(gasto.getTipo());
        dto.setDescripcion(gasto.getDescripcion());
        dto.setValor(gasto.getValor());
        dto.setAplica(gasto.getAplica());
        return dto;
    }
}
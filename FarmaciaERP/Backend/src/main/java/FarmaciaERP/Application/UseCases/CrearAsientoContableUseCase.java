package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.CrearAsientoContableRequest;
import FarmaciaERP.Application.DTOs.Request.CrearLineaAsientoRequest;
import FarmaciaERP.Application.DTOs.Response.AsientoContableResponse;
import FarmaciaERP.Domain.Entities.AsientoContable;
import FarmaciaERP.Domain.Entities.CentroCosto;
import FarmaciaERP.Domain.Entities.LineaAsiento;
import FarmaciaERP.Domain.Entities.SubcuentaDivisionaria;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IAsientoContableRepository;
import FarmaciaERP.Domain.Repositories.ICentroCostoRepository;
import FarmaciaERP.Domain.Repositories.ISubcuentaDivisionariaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CrearAsientoContableUseCase {

    private final IAsientoContableRepository asientoRepository;
    private final ISubcuentaDivisionariaRepository subcuentaRepository;
    private final ICentroCostoRepository centroCostoRepository;

    public CrearAsientoContableUseCase(IAsientoContableRepository asientoRepository,
                                       ISubcuentaDivisionariaRepository subcuentaRepository,
                                       ICentroCostoRepository centroCostoRepository) {
        this.asientoRepository = asientoRepository;
        this.subcuentaRepository = subcuentaRepository;
        this.centroCostoRepository = centroCostoRepository;
    }

    @Transactional
    public AsientoContableResponse ejecutar(CrearAsientoContableRequest request) {
        if (asientoRepository.existsByNumero(request.getNumero())) {
            throw new BadRequestException("Ya existe un asiento contable registrado con el nÃºmero " + request.getNumero());
        }

        List<LineaAsiento> lineasDomain = new ArrayList<>();
        for (CrearLineaAsientoRequest lReq : request.getLineas()) {
            SubcuentaDivisionaria subcuenta = subcuentaRepository.findById(lReq.getSubcuentaId())
                    .orElseThrow(() -> new BadRequestException("Subcuenta no encontrada: " + lReq.getSubcuentaId()));

            CentroCosto centroCosto = null;
            if (lReq.getCentroCostoId() != null) {
                centroCosto = centroCostoRepository.findById(lReq.getCentroCostoId())
                        .orElseThrow(() -> new BadRequestException("Centro de costo no encontrado: " + lReq.getCentroCostoId()));
            }

            LineaAsiento linea = new LineaAsiento(subcuenta, centroCosto, lReq.getDebe(), lReq.getHaber(), lReq.getGlosaDetalle());
            lineasDomain.add(linea);
        }

        AsientoContable asiento = new AsientoContable(
                request.getNumero(),
                request.getFechaContable(),
                request.getGlosa(),
                request.getTipoAsiento(),
                lineasDomain
        );

        AsientoContable guardado = asientoRepository.save(asiento);
        return AsientoContableResponseAssembler.toResponse(guardado);
    }
}
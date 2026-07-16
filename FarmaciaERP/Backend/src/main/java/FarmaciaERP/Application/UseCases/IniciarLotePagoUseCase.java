package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.IniciarLotePagoRequest;
import FarmaciaERP.Application.DTOs.Response.LotePagoTesoreriaResponse;
import FarmaciaERP.Domain.Entities.AjusteContableRegularizacion;
import FarmaciaERP.Domain.Entities.LotePagoTesoreria;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IAjusteContableRegularizacionRepository;
import FarmaciaERP.Domain.Repositories.ILotePagoTesoreriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * FI-AP.04 Fase 04 - Inicio del armado del lote: agrupa los ajustes
 * contables ya regularizados (Fase 03) para preparar el lote de pagos
 * (F110 SAP).
 */
@Service
public class IniciarLotePagoUseCase {

    private final ILotePagoTesoreriaRepository lotePagoTesoreriaRepository;
    private final IAjusteContableRegularizacionRepository ajusteContableRegularizacionRepository;

    public IniciarLotePagoUseCase(ILotePagoTesoreriaRepository lotePagoTesoreriaRepository,
                                   IAjusteContableRegularizacionRepository ajusteContableRegularizacionRepository) {
        this.lotePagoTesoreriaRepository = lotePagoTesoreriaRepository;
        this.ajusteContableRegularizacionRepository = ajusteContableRegularizacionRepository;
    }

    @Transactional
    public LotePagoTesoreriaResponse ejecutar(IniciarLotePagoRequest request) {
        if (request.getAjusteContableIds() == null || request.getAjusteContableIds().isEmpty()) {
            throw new BadRequestException(
                    "Debe indicarse al menos un ajuste contable para armar el lote de pagos");
        }

        List<AjusteContableRegularizacion> ajustes = request.getAjusteContableIds().stream()
                .map(id -> ajusteContableRegularizacionRepository.findById(id)
                        .orElseThrow(() -> new BadRequestException("Ajuste contable no encontrado: " + id)))
                .toList();

        // RN-AP4-01: un ajuste contable ya incluido en un lote no puede reutilizarse en otro.
        for (AjusteContableRegularizacion ajuste : ajustes) {
            if (lotePagoTesoreriaRepository.existsByAjusteContableRegularizacionId(ajuste.getId())) {
                throw new BadRequestException(
                        "El ajuste contable " + ajuste.getNumero() + " ya forma parte de otro lote de pagos");
            }
        }

        String numero = generarNumeroLote();
        LotePagoTesoreria lote = LotePagoTesoreria.iniciar(numero, ajustes);

        LotePagoTesoreria guardado = lotePagoTesoreriaRepository.save(lote);
        return LotePagoTesoreriaResponseAssembler.toResponse(guardado);
    }

    private String generarNumeroLote() {
        int correlativo = ThreadLocalRandom.current().nextInt(1000, 9999);
        return "F110-" + Year.now().getValue() + "-" + correlativo;
    }
}

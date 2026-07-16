package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.CrearConvenioRequest;
import FarmaciaERP.Application.DTOs.Request.ItemConvenioRequest;
import FarmaciaERP.Application.DTOs.Response.ConvenioResponse;
import FarmaciaERP.Domain.Entities.Convenio;
import FarmaciaERP.Domain.Entities.ItemConvenio;
import FarmaciaERP.Domain.Entities.Medicamento;
import FarmaciaERP.Domain.Entities.Proveedor;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IConvenioRepository;
import FarmaciaERP.Domain.Repositories.IMedicamentoRepository;
import FarmaciaERP.Domain.Repositories.IProveedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * LOG.02 - RN-MM-001 / RN-MM-004: registra el Contrato Marco (ME33K) y sus
 * precios pactados (Info-Record ME11) que respaldarán la Fase 02.
 */
@Service
public class CrearConvenioUseCase {

    private final IConvenioRepository convenioRepository;
    private final IProveedorRepository proveedorRepository;
    private final IMedicamentoRepository medicamentoRepository;

    public CrearConvenioUseCase(IConvenioRepository convenioRepository,
                                 IProveedorRepository proveedorRepository,
                                 IMedicamentoRepository medicamentoRepository) {
        this.convenioRepository = convenioRepository;
        this.proveedorRepository = proveedorRepository;
        this.medicamentoRepository = medicamentoRepository;
    }

    @Transactional
    public ConvenioResponse ejecutar(CrearConvenioRequest request) {
        if (convenioRepository.findByNumero(request.getNumero()).isPresent()) {
            throw new BadRequestException("Ya existe un convenio con el número " + request.getNumero());
        }

        Proveedor proveedor = proveedorRepository.findById(request.getProveedorId())
                .orElseThrow(() -> new BadRequestException("Proveedor no encontrado: " + request.getProveedorId()));

        List<ItemConvenio> items = new ArrayList<>();
        if (request.getItemsPactados() != null) {
            for (ItemConvenioRequest itemRequest : request.getItemsPactados()) {
                Medicamento medicamento = medicamentoRepository.findById(itemRequest.getMedicamentoId())
                        .orElseThrow(() -> new BadRequestException(
                                "Medicamento no encontrado: " + itemRequest.getMedicamentoId()));
                items.add(new ItemConvenio(medicamento, itemRequest.getPrecioPactado()));
            }
        }

        Convenio convenio = new Convenio(request.getNumero(), proveedor, request.getFechaInicio(),
                request.getFechaFin(), items);
        Convenio guardado = convenioRepository.save(convenio);
        return ConvenioResponseAssembler.toResponse(guardado);
    }
}

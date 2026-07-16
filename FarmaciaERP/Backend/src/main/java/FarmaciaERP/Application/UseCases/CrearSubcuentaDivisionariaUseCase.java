package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Request.CrearSubcuentaDivisionariaRequest;
import FarmaciaERP.Application.DTOs.Response.SubcuentaDivisionariaResponse;
import FarmaciaERP.Domain.Entities.Cuenta;
import FarmaciaERP.Domain.Entities.SubcuentaDivisionaria;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.ICuentaRepository;
import FarmaciaERP.Domain.Repositories.ISubcuentaDivisionariaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CrearSubcuentaDivisionariaUseCase {

    private final ISubcuentaDivisionariaRepository subcuentaRepository;
    private final ICuentaRepository cuentaRepository;

    public CrearSubcuentaDivisionariaUseCase(ISubcuentaDivisionariaRepository subcuentaRepository,
                                              ICuentaRepository cuentaRepository) {
        this.subcuentaRepository = subcuentaRepository;
        this.cuentaRepository = cuentaRepository;
    }

    @Transactional
    public SubcuentaDivisionariaResponse ejecutar(CrearSubcuentaDivisionariaRequest request) {
        if (subcuentaRepository.existsByCodigo(request.getCodigo())) {
            throw new BadRequestException(
                    "Ya existe una subcuenta divisionaria registrada con el cÃ³digo " + request.getCodigo());
        }
        Cuenta cuenta = cuentaRepository.findById(request.getCuentaId())
                .orElseThrow(() -> new BadRequestException("Cuenta no encontrada: " + request.getCuentaId()));

        SubcuentaDivisionaria subcuenta = new SubcuentaDivisionaria(request.getCodigo(), request.getNombre(), cuenta);
        return SubcuentaDivisionariaResponseAssembler.toResponse(subcuentaRepository.save(subcuenta));
    }
}
package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Application.DTOs.Response.EmpleadoResponse;
import FarmaciaERP.Domain.Enums.EstadoEmpleado;
import FarmaciaERP.Domain.Repositories.IEmpleadoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * RRHH.01 - Contratación: consultas y filtro por código, nombre, DNI o rol
 * (espejo del selector "Buscar por" del prototipo).
 */
@Service
public class BuscarEmpleadoUseCase {

    private final IEmpleadoRepository empleadoRepository;

    public BuscarEmpleadoUseCase(IEmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    public Optional<EmpleadoResponse> porId(Long id) {
        return empleadoRepository.findById(id).map(EmpleadoResponseAssembler::toResponse);
    }

    public List<EmpleadoResponse> todos() {
        return empleadoRepository.findAll().stream().map(EmpleadoResponseAssembler::toResponse).toList();
    }

    public List<EmpleadoResponse> porEstado(EstadoEmpleado estado) {
        return empleadoRepository.findByEstado(estado).stream().map(EmpleadoResponseAssembler::toResponse).toList();
    }

    public List<EmpleadoResponse> buscar(String texto) {
        return empleadoRepository.buscar(texto).stream().map(EmpleadoResponseAssembler::toResponse).toList();
    }
}

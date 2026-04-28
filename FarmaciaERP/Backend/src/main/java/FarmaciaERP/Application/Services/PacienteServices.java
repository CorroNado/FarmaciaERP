package FarmaciaERP.Application.Services;

import FarmaciaERP.Application.UseCases.ActualizarClienteUseCase;
import FarmaciaERP.Application.UseCases.BuscarClienteUseCase;
import FarmaciaERP.Application.UseCases.CrearClienteUseCase;
import FarmaciaERP.Application.UseCases.EliminarClienteUseCase;
import org.springframework.stereotype.Service;

@Service
public class PacienteServices {
    private final CrearClienteUseCase crearClienteUseCase;
    private final ActualizarClienteUseCase actualizarClienteUseCase;
    private final EliminarClienteUseCase eliminarClienteUseCase;
    private final BuscarClienteUseCase buscarClienteUseCase;

    public PacienteServices(
            CrearClienteUseCase crearClienteUseCase,
            ActualizarClienteUseCase actualizarClienteUseCase,
            EliminarClienteUseCase eliminarClienteUseCase,
            BuscarClienteUseCase buscarClienteUseCase) {
        this.crearClienteUseCase = crearClienteUseCase;
        this.actualizarClienteUseCase = actualizarClienteUseCase;
        this.eliminarClienteUseCase = eliminarClienteUseCase;
        this.buscarClienteUseCase = buscarClienteUseCase;
    }


}

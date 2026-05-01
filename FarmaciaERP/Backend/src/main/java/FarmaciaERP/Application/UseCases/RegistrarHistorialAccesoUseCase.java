package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Domain.Entities.HistorialAcceso;
import FarmaciaERP.Domain.Repositories.IHistorialAccesoRepository;
import FarmaciaERP.Domain.ValueObjects.Email;
import org.springframework.stereotype.Service;

@Service
public class RegistrarHistorialAccesoUseCase {

    private final IHistorialAccesoRepository repository;

    public RegistrarHistorialAccesoUseCase(
            IHistorialAccesoRepository repository) {

        this.repository = repository;
    }

    public void ejecutar(
            Long usuarioId,
            Email email,
            String accion,
            String ip) {

        HistorialAcceso historial =
                new HistorialAcceso(
                        usuarioId,
                        email,
                        accion,
                        ip
                );

        repository.save(historial);
    }
}
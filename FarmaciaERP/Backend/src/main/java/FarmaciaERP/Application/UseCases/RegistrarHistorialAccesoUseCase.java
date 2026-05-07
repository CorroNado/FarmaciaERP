package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Domain.Entities.HistorialAcceso;
import FarmaciaERP.Domain.Enums.AccionAcceso;
import FarmaciaERP.Domain.Repositories.IHistorialAccesoRepository;
import FarmaciaERP.Domain.ValueObjects.Email;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class RegistrarHistorialAccesoUseCase {

    private final IHistorialAccesoRepository historialRepository;

    public RegistrarHistorialAccesoUseCase(IHistorialAccesoRepository historialRepository) {
        this.historialRepository = historialRepository;
    }

    public void execute(Long usuarioId, AccionAcceso accion,String ip, String userAgent) {

        HistorialAcceso historial = new HistorialAcceso(usuarioId, accion, ip, userAgent);
        historialRepository.save(historial);
    }

}
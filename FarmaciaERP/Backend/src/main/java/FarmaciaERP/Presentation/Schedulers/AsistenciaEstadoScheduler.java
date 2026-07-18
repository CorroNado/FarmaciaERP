package FarmaciaERP.Presentation.Schedulers;

import FarmaciaERP.Application.UseCases.RRHH.ActualizarEstadosDinamicosUseCase;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * RRHH.02 - Control de Asistencia: recalcula cada minuto el estado dinámico
 * de los turnos aún no marcados (espejo de updateAllStatuses() del
 * prototipo, que se evaluaba en cada refresco de la pantalla).
 */
@Component
public class AsistenciaEstadoScheduler {

    private final ActualizarEstadosDinamicosUseCase actualizarEstadosDinamicosUseCase;

    public AsistenciaEstadoScheduler(ActualizarEstadosDinamicosUseCase actualizarEstadosDinamicosUseCase) {
        this.actualizarEstadosDinamicosUseCase = actualizarEstadosDinamicosUseCase;
    }

    @Scheduled(fixedRate = 60_000)
    public void monitorearEstadosDeAsistencia() {
        actualizarEstadosDinamicosUseCase.ejecutar();
    }
}

package FarmaciaERP.Presentation.Schedulers;

import FarmaciaERP.Application.UseCases.EjecutarBajasProgramadasUseCase;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * RRHH.01 - Contratación: ejecuta cada minuto el monitoreo de bajas
 * programadas (espejo de checkScheduledTerminations del prototipo, que se
 * evaluaba en cada refresco de la pantalla de Asistencia).
 */
@Component
public class BajaProgramadaScheduler {

    private final EjecutarBajasProgramadasUseCase ejecutarBajasProgramadasUseCase;

    public BajaProgramadaScheduler(EjecutarBajasProgramadasUseCase ejecutarBajasProgramadasUseCase) {
        this.ejecutarBajasProgramadasUseCase = ejecutarBajasProgramadasUseCase;
    }

    @Scheduled(fixedRate = 60_000)
    public void monitorearBajasProgramadas() {
        ejecutarBajasProgramadasUseCase.ejecutar();
    }
}

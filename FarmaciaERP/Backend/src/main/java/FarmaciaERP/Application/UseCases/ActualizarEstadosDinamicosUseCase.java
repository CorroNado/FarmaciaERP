package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Domain.Entities.RegistroAsistencia;
import FarmaciaERP.Domain.Repositories.IRegistroAsistenciaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * RRHH.02 - Control de Asistencia: recalcula los estados dinámicos
 * (Programado → Pendiente → Falta Injustificada) de los turnos aún no
 * marcados, espejo de updateAllStatuses() del prototipo.
 */
@Service
public class ActualizarEstadosDinamicosUseCase {

    private final IRegistroAsistenciaRepository registroAsistenciaRepository;

    public ActualizarEstadosDinamicosUseCase(IRegistroAsistenciaRepository registroAsistenciaRepository) {
        this.registroAsistenciaRepository = registroAsistenciaRepository;
    }

    @Transactional
    public void ejecutar() {
        LocalDateTime ahora = LocalDateTime.now();
        List<RegistroAsistencia> pendientes = registroAsistenciaRepository.findPendientesDeMarcacion();
        for (RegistroAsistencia registro : pendientes) {
            registro.actualizarEstadoDinamico(ahora);
            registroAsistenciaRepository.save(registro);
        }
    }
}

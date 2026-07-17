package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.RegistroAsistencia;
import FarmaciaERP.Infrastucture.Persistence.Entities.EmpleadoJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.RegistroAsistenciaJPA;

public class RegistroAsistenciaMapper {

    /**
     * `empleadoRef` debe ser una instancia YA GESTIONADA por JPA.
     */
    public static RegistroAsistenciaJPA ToEntity(RegistroAsistencia registro, EmpleadoJPA empleadoRef) {
        RegistroAsistenciaJPA entity = new RegistroAsistenciaJPA();
        entity.setId(registro.getId());
        entity.setEmpleado(empleadoRef);
        entity.setFecha(registro.getFecha());
        entity.setTurno(registro.getTurno());
        entity.setHoraEntrada(registro.getHoraEntrada());
        entity.setHoraSalida(registro.getHoraSalida());
        entity.setCheckinTimestamp(registro.getCheckinTimestamp());
        entity.setHorasTrabajadas(registro.getHorasTrabajadas());
        entity.setHorasExtras(registro.getHorasExtras());
        entity.setFactorExtra(registro.getFactorExtra());
        entity.setEstado(registro.getEstado());
        entity.setRegistrado(registro.isRegistrado());
        entity.setJustificado(registro.isJustificado());
        entity.setMotivoJustificacion(registro.getMotivoJustificacion());
        entity.setFechaCreacion(registro.getFechaCreacion());
        return entity;
    }

    public static RegistroAsistencia ToDomain(RegistroAsistenciaJPA entity) {
        return RegistroAsistencia.reconstruir(
                entity.getId(),
                EmpleadoMapper.ToDomain(entity.getEmpleado()),
                entity.getFecha(),
                entity.getTurno(),
                entity.getHoraEntrada(),
                entity.getHoraSalida(),
                entity.getCheckinTimestamp(),
                entity.getHorasTrabajadas(),
                entity.getHorasExtras(),
                entity.getFactorExtra(),
                entity.getEstado(),
                entity.isRegistrado(),
                entity.isJustificado(),
                entity.getMotivoJustificacion(),
                entity.getFechaCreacion()
        );
    }
}

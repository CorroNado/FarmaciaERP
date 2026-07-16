package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.RecetaMedicaAR;
import FarmaciaERP.Infrastucture.Persistence.Entities.ContabilizacionARJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.RecetaMedicaARJPA;

public class RecetaMedicaARMapper {

    /**
     * contabilizacionARRef debe ser una referencia YA GESTIONADA por JPA.
     */
    public static RecetaMedicaARJPA ToEntity(RecetaMedicaAR receta, ContabilizacionARJPA contabilizacionARRef) {
        RecetaMedicaARJPA entity = new RecetaMedicaARJPA();
        entity.setId(receta.getId());
        entity.setNumero(receta.getNumero());
        entity.setContabilizacionAR(contabilizacionARRef);
        entity.setMedicamento(receta.getMedicamento());
        entity.setAseguradora(receta.getAseguradora());
        entity.setMontoDeclarado(receta.getMontoDeclarado());
        entity.setMontoPreliquidado(receta.getMontoPreliquidado());
        entity.setEstado(receta.getEstado());
        entity.setMotivoRechazo(receta.getMotivoRechazo());
        entity.setInconsistencia(receta.getInconsistencia());
        entity.setFecha(receta.getFecha());
        return entity;
    }

    public static RecetaMedicaAR ToDomain(RecetaMedicaARJPA entity) {
        return RecetaMedicaAR.reconstruir(
                entity.getId(),
                entity.getNumero(),
                ContabilizacionARMapper.ToDomain(entity.getContabilizacionAR()),
                entity.getMedicamento(),
                entity.getAseguradora(),
                entity.getMontoDeclarado(),
                entity.getMontoPreliquidado(),
                entity.getEstado(),
                entity.getMotivoRechazo(),
                entity.getInconsistencia(),
                entity.getFecha()
        );
    }
}

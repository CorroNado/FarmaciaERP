package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.DetalleDevolucion;
import FarmaciaERP.Infrastucture.Persistence.Entities.DetalleDevolucionJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.DevolucionJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.MedicamentoJPA;

public class DetalleDevolucionMapper {

    /**
     * medicamentoRef debe ser una referencia YA GESTIONADA por JPA (obtenida con
     * getReferenceById/findById) para no intentar insertar un Medicamento duplicado.
     */
    public static DetalleDevolucionJPA ToEntity(DetalleDevolucion detalle, DevolucionJPA devolucion, MedicamentoJPA medicamentoRef) {
        DetalleDevolucionJPA entity = new DetalleDevolucionJPA();
        entity.setId(detalle.getId());
        entity.setDevolucion(devolucion);
        entity.setMedicamento(medicamentoRef);
        entity.setCantidad(detalle.getCantidad());
        entity.setPrecioUnitario(detalle.getPrecioUnitario());
        return entity;
    }

    public static DetalleDevolucion ToDomain(DetalleDevolucionJPA entity) {
        DetalleDevolucion detalle = new DetalleDevolucion(
                MedicamentoMapper.ToDomain(entity.getMedicamento()),
                entity.getCantidad(),
                entity.getPrecioUnitario()
        );
        detalle.setId(entity.getId());
        return detalle;
    }
}

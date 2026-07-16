package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.DetalleCotizacion;
import FarmaciaERP.Infrastucture.Persistence.Entities.CotizacionJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.DetalleCotizacionJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.MedicamentoJPA;

public class DetalleCotizacionMapper {

    /**
     * medicamentoRef debe ser una referencia YA GESTIONADA por JPA (obtenida con
     * getReferenceById/findById) para no intentar insertar un Medicamento duplicado.
     */
    public static DetalleCotizacionJPA ToEntity(DetalleCotizacion detalle, CotizacionJPA cotizacion, MedicamentoJPA medicamentoRef) {
        DetalleCotizacionJPA entity = new DetalleCotizacionJPA();
        entity.setId(detalle.getId());
        entity.setCotizacion(cotizacion);
        entity.setMedicamento(medicamentoRef);
        entity.setCantidad(detalle.getCantidad());
        entity.setPrecioUnitario(detalle.getPrecioUnitario());
        return entity;
    }

    public static DetalleCotizacion ToDomain(DetalleCotizacionJPA entity) {
        DetalleCotizacion detalle = new DetalleCotizacion(
                MedicamentoMapper.ToDomain(entity.getMedicamento()),
                entity.getCantidad(),
                entity.getPrecioUnitario()
        );
        detalle.setId(entity.getId());
        return detalle;
    }
}

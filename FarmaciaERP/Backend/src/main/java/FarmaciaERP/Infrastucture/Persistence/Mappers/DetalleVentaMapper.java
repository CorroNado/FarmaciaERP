package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.DetalleVenta;
import FarmaciaERP.Infrastucture.Persistence.Entities.DetalleVentaJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.MedicamentoJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.VentaJPA;

public class DetalleVentaMapper {

    /**
     * medicamentoRef debe ser una referencia YA GESTIONADA por JPA (obtenida con
     * getReferenceById/findById) para no intentar insertar un Medicamento duplicado.
     */
    public static DetalleVentaJPA ToEntity(DetalleVenta detalle, VentaJPA venta, MedicamentoJPA medicamentoRef) {
        DetalleVentaJPA entity = new DetalleVentaJPA();
        entity.setId(detalle.getId());
        entity.setVenta(venta);
        entity.setMedicamento(medicamentoRef);
        entity.setCantidad(detalle.getCantidad());
        entity.setPrecioUnitario(detalle.getPrecioUnitario());
        return entity;
    }

    public static DetalleVenta ToDomain(DetalleVentaJPA entity) {
        DetalleVenta detalle = new DetalleVenta(
                MedicamentoMapper.ToDomain(entity.getMedicamento()),
                entity.getCantidad(),
                entity.getPrecioUnitario()
        );
        detalle.setId(entity.getId());
        return detalle;
    }
}

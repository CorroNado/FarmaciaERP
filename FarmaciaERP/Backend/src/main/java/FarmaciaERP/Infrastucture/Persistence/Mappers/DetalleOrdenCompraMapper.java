package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.DetalleOrdenCompra;
import FarmaciaERP.Infrastucture.Persistence.Entities.DetalleOrdenCompraJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.MedicamentoJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.OrdenCompraJPA;

public class DetalleOrdenCompraMapper {

    public static DetalleOrdenCompraJPA ToEntity(DetalleOrdenCompra detalle, OrdenCompraJPA ordenRef, MedicamentoJPA medicamentoRef) {
        DetalleOrdenCompraJPA entity = new DetalleOrdenCompraJPA();
        entity.setId(detalle.getId());
        entity.setOrdenCompra(ordenRef);
        entity.setMedicamento(medicamentoRef);
        entity.setCantidad(detalle.getCantidad());
        entity.setPrecioUnitario(detalle.getPrecioUnitario());
        return entity;
    }

    public static DetalleOrdenCompra ToDomain(DetalleOrdenCompraJPA entity) {
        return new DetalleOrdenCompra(
                MedicamentoMapper.ToDomain(entity.getMedicamento()),
                entity.getCantidad(),
                entity.getPrecioUnitario()
        );
    }
}

package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.DetalleSolPed;
import FarmaciaERP.Infrastucture.Persistence.Entities.DetalleSolPedJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.MedicamentoJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.SolicitudPedidoJPA;

public class DetalleSolPedMapper {

    public static DetalleSolPedJPA ToEntity(DetalleSolPed detalle, SolicitudPedidoJPA solPedRef, MedicamentoJPA medicamentoRef) {
        DetalleSolPedJPA entity = new DetalleSolPedJPA();
        entity.setId(detalle.getId());
        entity.setSolicitudPedido(solPedRef);
        entity.setMedicamento(medicamentoRef);
        entity.setStockActual(detalle.getStockActual());
        entity.setStockMinimo(detalle.getStockMinimo());
        entity.setCantidadSugerida(detalle.getCantidadSugerida());
        entity.setPrecioUnitario(detalle.getPrecioUnitario());
        return entity;
    }

    public static DetalleSolPed ToDomain(DetalleSolPedJPA entity) {
        return new DetalleSolPed(
                MedicamentoMapper.ToDomain(entity.getMedicamento()),
                entity.getStockActual(),
                entity.getStockMinimo(),
                entity.getCantidadSugerida(),
                entity.getPrecioUnitario()
        );
    }
}

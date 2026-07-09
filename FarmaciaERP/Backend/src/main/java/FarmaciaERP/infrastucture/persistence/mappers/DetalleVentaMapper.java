package FarmaciaERP.infrastucture.persistence.mappers;

import FarmaciaERP.domain.entities.DetalleVenta;
import FarmaciaERP.infrastucture.persistence.entities.DetalleVentaJPA;
import FarmaciaERP.infrastucture.persistence.entities.MedicamentoJPA;
import FarmaciaERP.infrastucture.persistence.entities.VentaJPA;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DetalleVentaMapper {

    private final MedicamentoMapper medicamentoMapper;

    /**
     * medicamentoRef debe ser una referencia YA GESTIONADA por JPA (obtenida con
     * getReferenceById/findById) para no intentar insertar un Medicamento duplicado.
     */
    public DetalleVentaJPA toEntity(DetalleVenta detalle, VentaJPA venta, MedicamentoJPA medicamentoRef) {
        DetalleVentaJPA entity = new DetalleVentaJPA();
        entity.setId(detalle.getId());
        entity.setVenta(venta);
        entity.setMedicamento(medicamentoRef);
        entity.setCantidad(detalle.getCantidad());
        entity.setPrecioUnitario(detalle.getPrecioUnitario());
        return entity;
    }

    public DetalleVenta toDomain(DetalleVentaJPA entity) {
        DetalleVenta detalle = new DetalleVenta(
                medicamentoMapper.toDomain(entity.getMedicamento()),
                entity.getCantidad(),
                entity.getPrecioUnitario()
        );
        detalle.setId(entity.getId());
        return detalle;
    }
}

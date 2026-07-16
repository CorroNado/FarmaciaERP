package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.DetalleDevolucion;
import FarmaciaERP.Domain.Entities.Devolucion;
import FarmaciaERP.Infrastucture.Persistence.Entities.DetalleDevolucionJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.DevolucionJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.MedicamentoJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.VentaJPA;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DevolucionMapper {

    /**
     * ventaRef y medicamentosRef (por id) deben ser referencias YA GESTIONADAS por JPA.
     */
    public static DevolucionJPA ToEntity(Devolucion devolucion, VentaJPA ventaRef, Map<Integer, MedicamentoJPA> medicamentosRef) {
        DevolucionJPA entity = new DevolucionJPA();
        entity.setId(devolucion.getId());
        entity.setVenta(ventaRef);
        entity.setFecha(devolucion.getFecha());
        entity.setMotivo(devolucion.getMotivo());
        entity.setAccion(devolucion.getAccion());

        List<DetalleDevolucionJPA> detallesJPA = devolucion.getDetalles().stream()
                .map(detalle -> DetalleDevolucionMapper.ToEntity(
                        detalle,
                        entity,
                        medicamentosRef.get(detalle.getMedicamento().getId())))
                .collect(Collectors.toList());
        entity.setDetalles(detallesJPA);
        return entity;
    }

    public static Devolucion ToDomain(DevolucionJPA entity) {
        Devolucion devolucion = new Devolucion();
        devolucion.setId(entity.getId());
        devolucion.setVenta(VentaMapper.ToDomain(entity.getVenta()));
        devolucion.setFecha(entity.getFecha());
        devolucion.setMotivo(entity.getMotivo());
        devolucion.setAccion(entity.getAccion());

        List<DetalleDevolucion> detalles = entity.getDetalles().stream()
                .map(DetalleDevolucionMapper::ToDomain)
                .collect(Collectors.toList());
        devolucion.setDetalles(detalles);
        return devolucion;
    }
}

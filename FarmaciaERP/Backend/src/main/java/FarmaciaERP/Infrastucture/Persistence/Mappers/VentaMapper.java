package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.DetalleVenta;
import FarmaciaERP.Domain.Entities.Venta;
import FarmaciaERP.Infrastucture.Persistence.Entities.ClienteJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.DetalleVentaJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.MedicamentoJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.VentaJPA;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VentaMapper {

    /**
     * clienteRef y medicamentosRef (por id) deben ser referencias YA GESTIONADAS por JPA.
     */
    public static VentaJPA ToEntity(Venta venta, ClienteJPA clienteRef, Map<Integer, MedicamentoJPA> medicamentosRef) {
        VentaJPA entity = new VentaJPA();
        entity.setId(venta.getId());
        entity.setCliente(clienteRef);
        entity.setFecha(venta.getFecha());
        entity.setEstado(venta.getEstado());
        entity.setMetodoPago(venta.getMetodoPago());
        entity.setTipoComprobante(venta.getTipoComprobante());
        entity.setNumeroComprobante(venta.getNumeroComprobante());

        List<DetalleVentaJPA> detallesJPA = venta.getDetalles().stream()
                .map(detalle -> DetalleVentaMapper.ToEntity(
                        detalle,
                        entity,
                        medicamentosRef.get(detalle.getMedicamento().getId())))
                .collect(Collectors.toList());
        entity.setDetalles(detallesJPA);
        return entity;
    }

    public static Venta ToDomain(VentaJPA entity) {
        Venta venta = new Venta();
        venta.setId(entity.getId());
        venta.setCliente(ClienteMapper.ToDomain(entity.getCliente()));
        venta.setFecha(entity.getFecha());
        venta.setEstado(entity.getEstado());
        venta.setMetodoPago(entity.getMetodoPago());
        venta.setTipoComprobante(entity.getTipoComprobante());
        venta.setNumeroComprobante(entity.getNumeroComprobante());

        List<DetalleVenta> detalles = entity.getDetalles().stream()
                .map(DetalleVentaMapper::ToDomain)
                .collect(Collectors.toList());
        venta.setDetalles(detalles);
        return venta;
    }
}

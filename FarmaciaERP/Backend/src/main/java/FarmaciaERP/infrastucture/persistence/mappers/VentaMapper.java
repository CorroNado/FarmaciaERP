package FarmaciaERP.infrastucture.persistence.mappers;

import FarmaciaERP.domain.entities.DetalleVenta;
import FarmaciaERP.domain.entities.Venta;
import FarmaciaERP.infrastucture.persistence.entities.ClienteJPA;
import FarmaciaERP.infrastucture.persistence.entities.DetalleVentaJPA;
import FarmaciaERP.infrastucture.persistence.entities.MedicamentoJPA;
import FarmaciaERP.infrastucture.persistence.entities.VentaJPA;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class VentaMapper {

    private final ClienteMapper clienteMapper;
    private final DetalleVentaMapper detalleVentaMapper;

    /**
     * clienteRef y medicamentosRef (por id) deben ser referencias YA GESTIONADAS por JPA.
     */
    public VentaJPA toEntity(Venta venta, ClienteJPA clienteRef, Map<Integer, MedicamentoJPA> medicamentosRef) {
        VentaJPA entity = new VentaJPA();
        entity.setId(venta.getId());
        entity.setCliente(clienteRef);
        entity.setFecha(venta.getFecha());
        entity.setEstado(venta.getEstado());
        entity.setMetodoPago(venta.getMetodoPago());
        entity.setTipoComprobante(venta.getTipoComprobante());
        entity.setNumeroComprobante(venta.getNumeroComprobante());

        List<DetalleVentaJPA> detallesJPA = venta.getDetalles().stream()
                .map(detalle -> detalleVentaMapper.toEntity(
                        detalle,
                        entity,
                        medicamentosRef.get(detalle.getMedicamento().getId())))
                .collect(Collectors.toList());
        entity.setDetalles(detallesJPA);
        return entity;
    }

    public Venta toDomain(VentaJPA entity) {
        Venta venta = new Venta();
        venta.setId(entity.getId());
        venta.setCliente(clienteMapper.toDomain(entity.getCliente()));
        venta.setFecha(entity.getFecha());
        venta.setEstado(entity.getEstado());
        venta.setMetodoPago(entity.getMetodoPago());
        venta.setTipoComprobante(entity.getTipoComprobante());
        venta.setNumeroComprobante(entity.getNumeroComprobante());

        List<DetalleVenta> detalles = entity.getDetalles().stream()
                .map(detalleVentaMapper::toDomain)
                .collect(Collectors.toList());
        venta.setDetalles(detalles);
        return venta;
    }
}

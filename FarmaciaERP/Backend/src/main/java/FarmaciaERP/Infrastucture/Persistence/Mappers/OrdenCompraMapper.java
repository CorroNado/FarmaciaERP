package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.OrdenCompra;
import FarmaciaERP.Infrastucture.Persistence.Entities.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrdenCompraMapper {

    /**
     * Todas las referencias (solPedRef, proveedorRef, convenioRef, medicamentosRef)
     * deben ser instancias YA GESTIONADAS por JPA.
     */
    public static OrdenCompraJPA ToEntity(OrdenCompra oc, SolicitudPedidoJPA solPedRef, ProveedorJPA proveedorRef,
                                           ConvenioJPA convenioRef, Map<Integer, MedicamentoJPA> medicamentosRef) {
        OrdenCompraJPA entity = new OrdenCompraJPA();
        entity.setId(oc.getId());
        entity.setNumero(oc.getNumero());
        entity.setSolicitudPedido(solPedRef);
        entity.setProveedor(proveedorRef);
        entity.setConvenio(convenioRef);
        entity.setFecha(oc.getFecha());
        entity.setFechaEntregaLimite(oc.getFechaEntregaLimite());
        entity.setCentroDestino(oc.getCentroDestino());
        entity.setEstado(oc.getEstado());
        entity.setFechaFirma(oc.getFechaFirma());

        List<DetalleOrdenCompraJPA> detalles = oc.getDetalles().stream()
                .map(detalle -> DetalleOrdenCompraMapper.ToEntity(detalle, entity,
                        medicamentosRef.get(detalle.getMedicamento().getId())))
                .collect(Collectors.toList());
        entity.setDetalles(detalles);
        return entity;
    }

    public static OrdenCompra ToDomain(OrdenCompraJPA entity) {
        OrdenCompra oc = new OrdenCompra();
        oc.setId(entity.getId());
        oc.setNumero(entity.getNumero());
        oc.setSolicitudPedido(SolicitudPedidoMapper.ToDomain(entity.getSolicitudPedido()));
        oc.setProveedor(ProveedorMapper.ToDomain(entity.getProveedor()));
        oc.setConvenio(ConvenioMapper.ToDomain(entity.getConvenio()));
        oc.setFecha(entity.getFecha());
        oc.setFechaEntregaLimite(entity.getFechaEntregaLimite());
        oc.setCentroDestino(entity.getCentroDestino());
        oc.setEstado(entity.getEstado());
        oc.setFechaFirma(entity.getFechaFirma());
        oc.setDetalles(entity.getDetalles().stream()
                .map(DetalleOrdenCompraMapper::ToDomain)
                .collect(Collectors.toList()));
        return oc;
    }
}

package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.SolicitudPedido;
import FarmaciaERP.Infrastucture.Persistence.Entities.ConvenioJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.DetalleSolPedJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.MedicamentoJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.ProveedorJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.SolicitudPedidoJPA;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SolicitudPedidoMapper {

    /**
     * medicamentosRef (por id) debe contener referencias YA GESTIONADAS por JPA.
     * proveedorRef / convenioRef pueden ser null mientras la SolPed esté en Fase 01.
     */
    public static SolicitudPedidoJPA ToEntity(SolicitudPedido solPed, Map<Integer, MedicamentoJPA> medicamentosRef,
                                               ProveedorJPA proveedorRef, ConvenioJPA convenioRef) {
        SolicitudPedidoJPA entity = new SolicitudPedidoJPA();
        entity.setId(solPed.getId());
        entity.setNumero(solPed.getNumero());
        entity.setFecha(solPed.getFecha());
        entity.setResponsable(solPed.getResponsable());
        entity.setCentroCosto(solPed.getCentroCosto());
        entity.setPresupuesto(solPed.getPresupuesto());
        entity.setEstado(solPed.getEstado());
        entity.setProveedor(proveedorRef);
        entity.setConvenio(convenioRef);
        entity.setMotivoRechazo(solPed.getMotivoRechazo());

        List<DetalleSolPedJPA> detalles = solPed.getDetalles().stream()
                .map(detalle -> DetalleSolPedMapper.ToEntity(detalle, entity,
                        medicamentosRef.get(detalle.getMedicamento().getId())))
                .collect(Collectors.toList());
        entity.setDetalles(detalles);
        return entity;
    }

    public static SolicitudPedido ToDomain(SolicitudPedidoJPA entity) {
        SolicitudPedido solPed = new SolicitudPedido();
        solPed.setId(entity.getId());
        solPed.setNumero(entity.getNumero());
        solPed.setFecha(entity.getFecha());
        solPed.setResponsable(entity.getResponsable());
        solPed.setCentroCosto(entity.getCentroCosto());
        solPed.setPresupuesto(entity.getPresupuesto());
        solPed.setEstado(entity.getEstado());
        solPed.setMotivoRechazo(entity.getMotivoRechazo());
        if (entity.getProveedor() != null) {
            solPed.setProveedor(ProveedorMapper.ToDomain(entity.getProveedor()));
        }
        if (entity.getConvenio() != null) {
            solPed.setConvenio(ConvenioMapper.ToDomain(entity.getConvenio()));
        }
        solPed.setDetalles(entity.getDetalles().stream()
                .map(DetalleSolPedMapper::ToDomain)
                .collect(Collectors.toList()));
        return solPed;
    }
}

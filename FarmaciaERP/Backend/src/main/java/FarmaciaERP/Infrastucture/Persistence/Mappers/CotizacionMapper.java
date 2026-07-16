package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.Cotizacion;
import FarmaciaERP.Domain.Entities.DetalleCotizacion;
import FarmaciaERP.Infrastucture.Persistence.Entities.ClienteJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.CotizacionJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.DetalleCotizacionJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.MedicamentoJPA;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CotizacionMapper {

    /**
     * clienteRef y medicamentosRef (por id) deben ser referencias YA GESTIONADAS por JPA.
     */
    public static CotizacionJPA ToEntity(Cotizacion cotizacion, ClienteJPA clienteRef, Map<Integer, MedicamentoJPA> medicamentosRef) {
        CotizacionJPA entity = new CotizacionJPA();
        entity.setId(cotizacion.getId());
        entity.setCliente(clienteRef);
        entity.setFecha(cotizacion.getFecha());
        entity.setVigenciaDias(cotizacion.getVigenciaDias());
        entity.setEstado(cotizacion.getEstado());
        entity.setMotivoRechazo(cotizacion.getMotivoRechazo());
        entity.setVentaGeneradaId(cotizacion.getVentaGeneradaId());

        List<DetalleCotizacionJPA> detallesJPA = cotizacion.getDetalles().stream()
                .map(detalle -> DetalleCotizacionMapper.ToEntity(
                        detalle,
                        entity,
                        medicamentosRef.get(detalle.getMedicamento().getId())))
                .collect(Collectors.toList());
        entity.setDetalles(detallesJPA);
        return entity;
    }

    public static Cotizacion ToDomain(CotizacionJPA entity) {
        Cotizacion cotizacion = new Cotizacion();
        cotizacion.setId(entity.getId());
        cotizacion.setCliente(ClienteMapper.ToDomain(entity.getCliente()));
        cotizacion.setFecha(entity.getFecha());
        cotizacion.setVigenciaDias(entity.getVigenciaDias());
        cotizacion.setEstado(entity.getEstado());
        cotizacion.setMotivoRechazo(entity.getMotivoRechazo());
        cotizacion.setVentaGeneradaId(entity.getVentaGeneradaId());

        List<DetalleCotizacion> detalles = entity.getDetalles().stream()
                .map(DetalleCotizacionMapper::ToDomain)
                .collect(Collectors.toList());
        cotizacion.setDetalles(detalles);
        return cotizacion;
    }
}

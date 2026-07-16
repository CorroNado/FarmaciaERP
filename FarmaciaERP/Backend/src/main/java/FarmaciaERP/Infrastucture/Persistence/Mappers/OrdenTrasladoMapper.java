package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.OrdenTraslado;
import FarmaciaERP.Infrastucture.Persistence.Entities.InspeccionCalidadJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.OrdenTrasladoJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.SucursalJPA;

public class OrdenTrasladoMapper {

    /**
     * `inspeccionCalidadRef` y `sucursalRef` deben ser instancias YA
     * GESTIONADAS por JPA.
     */
    public static OrdenTrasladoJPA ToEntity(OrdenTraslado orden, InspeccionCalidadJPA inspeccionCalidadRef,
                                             SucursalJPA sucursalRef) {
        OrdenTrasladoJPA entity = new OrdenTrasladoJPA();
        entity.setId(orden.getId());
        entity.setNumero(orden.getNumero());
        entity.setInspeccionCalidad(inspeccionCalidadRef);
        entity.setSucursalDestino(sucursalRef);
        entity.setGuiaRemision(orden.getGuiaRemision());
        entity.setEstado(orden.getEstado());
        entity.setFechaDespacho(orden.getFechaDespacho());
        entity.setFechaRecepcion(orden.getFechaRecepcion());
        return entity;
    }

    public static OrdenTraslado ToDomain(OrdenTrasladoJPA entity) {
        return OrdenTraslado.reconstruir(
                entity.getId(),
                entity.getNumero(),
                InspeccionCalidadMapper.ToDomain(entity.getInspeccionCalidad()),
                SucursalMapper.ToDomain(entity.getSucursalDestino()),
                entity.getGuiaRemision(),
                entity.getEstado(),
                entity.getFechaDespacho(),
                entity.getFechaRecepcion()
        );
    }
}

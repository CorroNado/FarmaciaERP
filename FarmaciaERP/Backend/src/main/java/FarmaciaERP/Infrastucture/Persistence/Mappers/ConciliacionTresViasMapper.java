package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.ConciliacionTresVias;
import FarmaciaERP.Infrastucture.Persistence.Entities.ConciliacionTresViasJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.EntradaMercanciaJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.FacturaMIROJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.OrdenCompraJPA;

public class ConciliacionTresViasMapper {

    /**
     * `ordenCompraRef`, `entradaMercanciaRef` y `facturaMIRORef` deben ser
     * instancias YA GESTIONADAS por JPA.
     */
    public static ConciliacionTresViasJPA ToEntity(ConciliacionTresVias conciliacion, OrdenCompraJPA ordenCompraRef,
                                                     EntradaMercanciaJPA entradaMercanciaRef, FacturaMIROJPA facturaMIRORef) {
        ConciliacionTresViasJPA entity = new ConciliacionTresViasJPA();
        entity.setId(conciliacion.getId());
        entity.setNumero(conciliacion.getNumero());
        entity.setOrdenCompra(ordenCompraRef);
        entity.setEntradaMercancia(entradaMercanciaRef);
        entity.setFacturaMIRO(facturaMIRORef);
        entity.setCantidadCoincide(conciliacion.isCantidadCoincide());
        entity.setPrecioCoincide(conciliacion.isPrecioCoincide());
        entity.setFacturaVinculada(conciliacion.isFacturaVinculada());
        entity.setQaAprobado(conciliacion.isQaAprobado());
        entity.setResultado(conciliacion.getResultado());
        entity.setFecha(conciliacion.getFecha());
        return entity;
    }

    public static ConciliacionTresVias ToDomain(ConciliacionTresViasJPA entity) {
        return ConciliacionTresVias.reconstruir(
                entity.getId(),
                entity.getNumero(),
                OrdenCompraMapper.ToDomain(entity.getOrdenCompra()),
                EntradaMercanciaMapper.ToDomain(entity.getEntradaMercancia()),
                FacturaMIROMapper.ToDomain(entity.getFacturaMIRO()),
                entity.isCantidadCoincide(),
                entity.isPrecioCoincide(),
                entity.isFacturaVinculada(),
                entity.isQaAprobado(),
                entity.getResultado(),
                entity.getFecha()
        );
    }
}

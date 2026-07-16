package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.LotePagoTesoreria;
import FarmaciaERP.Infrastucture.Persistence.Entities.AjusteContableRegularizacionJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.LotePagoTesoreriaJPA;

import java.util.List;

public class LotePagoTesoreriaMapper {

    /**
     * `ajustesContablesRef` deben ser instancias YA GESTIONADAS por JPA.
     */
    public static LotePagoTesoreriaJPA ToEntity(LotePagoTesoreria lote,
                                                 List<AjusteContableRegularizacionJPA> ajustesContablesRef) {
        LotePagoTesoreriaJPA entity = new LotePagoTesoreriaJPA();
        entity.setId(lote.getId());
        entity.setNumero(lote.getNumero());
        entity.setAjustesContables(ajustesContablesRef);
        entity.setMontoNetoRegularizado(lote.getMontoNetoRegularizado());
        entity.setProveedoresCriticosPriorizados(lote.isProveedoresCriticosPriorizados());
        entity.setDescuentoProntoPagoNegociado(lote.isDescuentoProntoPagoNegociado());
        entity.setDescuentoProntoPagoPct(lote.getDescuentoProntoPagoPct());
        entity.setLotePreparado(lote.isLotePreparado());
        entity.setMontoLote(lote.getMontoLote());
        entity.setFondosVerificados(lote.isFondosVerificados());
        entity.setRevisionesComite(lote.getRevisionesComite());
        entity.setLoteCorregido(lote.isLoteCorregido());
        entity.setAprobadoPorComite(lote.isAprobadoPorComite());
        entity.setPagosConciliadosGestion(lote.isPagosConciliadosGestion());
        entity.setEstado(lote.getEstado());
        entity.setFecha(lote.getFecha());
        return entity;
    }

    public static LotePagoTesoreria ToDomain(LotePagoTesoreriaJPA entity) {
        return LotePagoTesoreria.reconstruir(
                entity.getId(),
                entity.getNumero(),
                entity.getAjustesContables().stream().map(AjusteContableRegularizacionMapper::ToDomain).toList(),
                entity.getMontoNetoRegularizado(),
                entity.isProveedoresCriticosPriorizados(),
                entity.isDescuentoProntoPagoNegociado(),
                entity.getDescuentoProntoPagoPct(),
                entity.isLotePreparado(),
                entity.getMontoLote(),
                entity.isFondosVerificados(),
                entity.getRevisionesComite(),
                entity.isLoteCorregido(),
                entity.isAprobadoPorComite(),
                entity.isPagosConciliadosGestion(),
                entity.getEstado(),
                entity.getFecha()
        );
    }
}

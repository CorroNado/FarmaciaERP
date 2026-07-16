package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.Pago;
import FarmaciaERP.Infrastucture.Persistence.Entities.ConciliacionTresViasJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.FacturaMIROJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.PagoJPA;

public class PagoMapper {

    /**
     * `facturaMIRORef` y `conciliacionTresViasRef` deben ser instancias YA
     * GESTIONADAS por JPA.
     */
    public static PagoJPA ToEntity(Pago pago, FacturaMIROJPA facturaMIRORef, ConciliacionTresViasJPA conciliacionTresViasRef) {
        PagoJPA entity = new PagoJPA();
        entity.setId(pago.getId());
        entity.setNumero(pago.getNumero());
        entity.setFacturaMIRO(facturaMIRORef);
        entity.setConciliacionTresVias(conciliacionTresViasRef);
        entity.setBanco(pago.getBanco());
        entity.setFechaPago(pago.getFechaPago());
        entity.setMonto(pago.getMonto());
        entity.setEstado(pago.getEstado());
        entity.setFecha(pago.getFecha());
        return entity;
    }

    public static Pago ToDomain(PagoJPA entity) {
        Pago pago = new Pago();
        pago.setId(entity.getId());
        pago.setNumero(entity.getNumero());
        pago.setFacturaMIRO(FacturaMIROMapper.ToDomain(entity.getFacturaMIRO()));
        pago.setConciliacionTresVias(ConciliacionTresViasMapper.ToDomain(entity.getConciliacionTresVias()));
        pago.setBanco(entity.getBanco());
        pago.setFechaPago(entity.getFechaPago());
        pago.setMonto(entity.getMonto());
        pago.setEstado(entity.getEstado());
        pago.setFecha(entity.getFecha());
        return pago;
    }
}

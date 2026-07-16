package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.FacturaMIRO;
import FarmaciaERP.Infrastucture.Persistence.Entities.FacturaMIROJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.OrdenCompraJPA;

public class FacturaMIROMapper {

    /**
     * `ordenCompraRef` debe ser una instancia YA GESTIONADA por JPA.
     */
    public static FacturaMIROJPA ToEntity(FacturaMIRO factura, OrdenCompraJPA ordenCompraRef) {
        FacturaMIROJPA entity = new FacturaMIROJPA();
        entity.setId(factura.getId());
        entity.setNumero(factura.getNumero());
        entity.setNumeroFactura(factura.getNumeroFactura());
        entity.setOrdenCompra(ordenCompraRef);
        entity.setFechaEmision(factura.getFechaEmision());
        entity.setMontoNeto(factura.getMontoNeto());
        entity.setIgv(factura.getIgv());
        entity.setMontoTotal(factura.getMontoTotal());
        entity.setEstado(factura.getEstado());
        entity.setFecha(factura.getFecha());
        return entity;
    }

    public static FacturaMIRO ToDomain(FacturaMIROJPA entity) {
        FacturaMIRO factura = new FacturaMIRO();
        factura.setId(entity.getId());
        factura.setNumero(entity.getNumero());
        factura.setNumeroFactura(entity.getNumeroFactura());
        factura.setOrdenCompra(OrdenCompraMapper.ToDomain(entity.getOrdenCompra()));
        factura.setFechaEmision(entity.getFechaEmision());
        factura.setMontoNeto(entity.getMontoNeto());
        factura.setIgv(entity.getIgv());
        factura.setMontoTotal(entity.getMontoTotal());
        factura.setEstado(entity.getEstado());
        factura.setFecha(entity.getFecha());
        return factura;
    }
}

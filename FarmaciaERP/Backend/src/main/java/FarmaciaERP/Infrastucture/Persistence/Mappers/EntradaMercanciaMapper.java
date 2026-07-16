package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.EntradaMercancia;
import FarmaciaERP.Infrastucture.Persistence.Entities.EntradaMercanciaJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.OrdenCompraJPA;

public class EntradaMercanciaMapper {

    /**
     * `ordenCompraRef` debe ser una instancia YA GESTIONADA por JPA.
     */
    public static EntradaMercanciaJPA ToEntity(EntradaMercancia entrada, OrdenCompraJPA ordenCompraRef) {
        EntradaMercanciaJPA entity = new EntradaMercanciaJPA();
        entity.setId(entrada.getId());
        entity.setNumero(entrada.getNumero());
        entity.setOrdenCompra(ordenCompraRef);
        entity.setLote(entrada.getLote());
        entity.setFechaVencimiento(entrada.getFechaVencimiento());
        entity.setTemperaturaArribo(entrada.getTemperaturaArribo());
        entity.setCantidadPedida(entrada.getCantidadPedida());
        entity.setCantidadRecibida(entrada.getCantidadRecibida());
        entity.setFecha(entrada.getFecha());
        entity.setEstado(entrada.getEstado());
        entity.setAlertaCadenaFrio(entrada.isAlertaCadenaFrio());
        return entity;
    }

    public static EntradaMercancia ToDomain(EntradaMercanciaJPA entity) {
        EntradaMercancia entrada = new EntradaMercancia();
        entrada.setId(entity.getId());
        entrada.setNumero(entity.getNumero());
        entrada.setOrdenCompra(OrdenCompraMapper.ToDomain(entity.getOrdenCompra()));
        entrada.setLote(entity.getLote());
        entrada.setFechaVencimiento(entity.getFechaVencimiento());
        entrada.setTemperaturaArribo(entity.getTemperaturaArribo());
        entrada.setCantidadPedida(entity.getCantidadPedida());
        entrada.setCantidadRecibida(entity.getCantidadRecibida());
        entrada.setFecha(entity.getFecha());
        entrada.setEstado(entity.getEstado());
        entrada.setAlertaCadenaFrio(entity.isAlertaCadenaFrio());
        return entrada;
    }
}

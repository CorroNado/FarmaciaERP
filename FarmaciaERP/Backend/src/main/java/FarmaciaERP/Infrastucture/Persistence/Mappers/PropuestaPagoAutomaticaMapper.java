package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.PropuestaPagoAutomatica;
import FarmaciaERP.Infrastucture.Persistence.Entities.LotePagoTesoreriaJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.PropuestaPagoAutomaticaJPA;

public class PropuestaPagoAutomaticaMapper {

    /**
     * `lotePagoTesoreriaRef` debe ser una instancia YA GESTIONADA por JPA.
     */
    public static PropuestaPagoAutomaticaJPA ToEntity(PropuestaPagoAutomatica propuesta,
                                                       LotePagoTesoreriaJPA lotePagoTesoreriaRef) {
        PropuestaPagoAutomaticaJPA entity = new PropuestaPagoAutomaticaJPA();
        entity.setId(propuesta.getId());
        entity.setNumero(propuesta.getNumero());
        entity.setLotePagoTesoreria(lotePagoTesoreriaRef);
        entity.setSociedad(propuesta.getSociedad());
        entity.setViaPago(propuesta.getViaPago());
        entity.setFechaPago(propuesta.getFechaPago());
        entity.setParametrosIntroducidos(propuesta.isParametrosIntroducidos());
        entity.setPropuestaEjecutada(propuesta.isPropuestaEjecutada());
        entity.setMontoPropuesta(propuesta.getMontoPropuesta());
        entity.setIntentos(propuesta.getIntentos());
        entity.setPropuestaCorrecta(propuesta.getPropuestaCorrecta());
        entity.setPropuestaAprobada(propuesta.isPropuestaAprobada());
        entity.setPagoEjecutado(propuesta.isPagoEjecutado());
        entity.setArchivosGenerados(propuesta.isArchivosGenerados());
        entity.setEstado(propuesta.getEstado());
        entity.setFecha(propuesta.getFecha());
        return entity;
    }

    public static PropuestaPagoAutomatica ToDomain(PropuestaPagoAutomaticaJPA entity) {
        return PropuestaPagoAutomatica.reconstruir(
                entity.getId(),
                entity.getNumero(),
                LotePagoTesoreriaMapper.ToDomain(entity.getLotePagoTesoreria()),
                entity.getSociedad(),
                entity.getViaPago(),
                entity.getFechaPago(),
                entity.isParametrosIntroducidos(),
                entity.isPropuestaEjecutada(),
                entity.getMontoPropuesta(),
                entity.getIntentos(),
                entity.getPropuestaCorrecta(),
                entity.isPropuestaAprobada(),
                entity.isPagoEjecutado(),
                entity.isArchivosGenerados(),
                entity.getEstado(),
                entity.getFecha()
        );
    }
}

package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.HistorialAcceso;
import FarmaciaERP.Infrastucture.Persistence.Entities.HistorialAccesoJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.UsuarioJPA;

public class HistorialAccesoMapper {
    private HistorialAccesoMapper() {}

    public static HistorialAccesoJPA toEntity(HistorialAcceso domain) {
        HistorialAccesoJPA entity = new HistorialAccesoJPA();
        entity.setId(domain.getId());
        entity.setAccion(domain.getAccion());
        entity.setIp(domain.getIp());
        entity.setUserAgent(domain.getUserAgent());
        entity.setFecha(domain.getFecha());
        UsuarioJPA usuarioRef = new UsuarioJPA();
        usuarioRef.setId(domain.getUsuarioId());
        entity.setUsuario(usuarioRef);
        return entity;
    }

    public static HistorialAcceso toDomain(HistorialAccesoJPA entity) {
        return new HistorialAcceso(
                entity.getId(),
                entity.getUsuario().getId(),
                entity.getAccion(),
                entity.getIp(),
                entity.getUserAgent(),
                entity.getFecha()
        );
    }
}

package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.Entities.CierreCaja;
import FarmaciaERP.Infrastucture.Persistence.Entities.CierreCajaJPA;
import FarmaciaERP.Infrastucture.Persistence.Entities.SucursalJPA;

public class CierreCajaMapper {

    /**
     * sucursalRef debe ser una referencia YA GESTIONADA por JPA.
     */
    public static CierreCajaJPA ToEntity(CierreCaja cierre, SucursalJPA sucursalRef) {
        CierreCajaJPA entity = new CierreCajaJPA();
        entity.setId(cierre.getId());
        entity.setNumero(cierre.getNumero());
        entity.setSucursal(sucursalRef);
        entity.setFecha(cierre.getFecha());
        entity.setReporteVentas(cierre.getReporteVentas());
        entity.setArqueo(cierre.getArqueo());
        entity.setDiferencia(cierre.getDiferencia());
        entity.setCuadra(cierre.getCuadra());
        entity.setJustificacion(cierre.getJustificacion());
        entity.setFisicosEnviados(cierre.isFisicosEnviados());
        entity.setCopago(cierre.getCopago());
        entity.setCoberturaAseg(cierre.getCoberturaAseg());
        entity.setEstado(cierre.getEstado());
        return entity;
    }

    public static CierreCaja ToDomain(CierreCajaJPA entity) {
        return CierreCaja.reconstruir(
                entity.getId(),
                entity.getNumero(),
                SucursalMapper.ToDomain(entity.getSucursal()),
                entity.getFecha(),
                entity.getReporteVentas(),
                entity.getArqueo(),
                entity.getDiferencia(),
                entity.getCuadra(),
                entity.getJustificacion(),
                entity.isFisicosEnviados(),
                entity.getCopago(),
                entity.getCoberturaAseg(),
                entity.getEstado()
        );
    }
}

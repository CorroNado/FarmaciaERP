package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.ValueObjects.Dni;
import FarmaciaERP.Infrastucture.Persistence.ValueObjects.DniEmb;

public class DniMapper {
    public static DniEmb toEmbeddable(Dni dni) {
        return dni == null ? null : new DniEmb(dni.getDni());
    }

    public static Dni toDomain(DniEmb embeddable) {
        return embeddable == null ? null : new Dni(embeddable.getDni());
    }
}

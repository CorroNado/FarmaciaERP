package FarmaciaERP.Infrastucture.Persistence.Mappers;

import FarmaciaERP.Domain.ValueObjects.FullName;
import FarmaciaERP.Infrastucture.Persistence.ValueObjects.FullNameEmb;

public class FullnameMapper {
    public static FullNameEmb toEmbeddable(FullName fullName) {
        return fullName == null ? null : new FullNameEmb(fullName.getNombres(),fullName.getApellidos());
    }

    public static FullName toDomain(FullNameEmb embeddable) {
        return embeddable == null ? null : new FullName(embeddable.getNombres(),embeddable.getApellidos());
    }
}

package FarmaciaERP.infrastucture.persistence.embeddable;

import FarmaciaERP.domain.enums.TelephoneType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;

@Embeddable
@Getter
public class TelephoneEmb {

    @Column(name = "prefix", nullable = false, length = 5)
    private String prefijo;

    @Column(name = "area_code", length = 5)
    private String codigoArea;

    @Column(name = "number", nullable = false, length = 15)
    private String numero;

    @Column(name = "description", length = 100)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TelephoneType tipo;

    @Column(name = "full_number")
    @Getter(AccessLevel.NONE)
    private String numeroCompleto;

    protected TelephoneEmb() {
        // JPA
    }

    public TelephoneEmb(String prefijo, String codigoArea, String numero, String descripcion, TelephoneType tipo) {
        this.prefijo = prefijo;
        this.codigoArea = codigoArea;
        this.numero = numero;
        this.descripcion = descripcion;
        this.tipo = tipo;

        String area = (codigoArea != null) ? codigoArea : "";
        this.numeroCompleto = prefijo + area + numero;
    }
}

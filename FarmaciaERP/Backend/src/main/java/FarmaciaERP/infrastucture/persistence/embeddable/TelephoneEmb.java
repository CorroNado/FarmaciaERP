package FarmaciaERP.infrastucture.persistence.embeddable;

import FarmaciaERP.domain.enums.TelephoneType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

@Embeddable
@Getter
public class TelephoneEmb {

    @Column(name = "prefijo", nullable = false, length = 5)
    private String prefijo;

    @Column(name = "codigo_area", length = 5)
    private String codigoArea;

    @Column(name = "numero", nullable = false, length = 15)
    private String numero;

    @Column(name = "descripcion", length = 100)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TelephoneType tipo;

    protected TelephoneEmb() {
        // JPA
    }

    public TelephoneEmb(String prefijo, String codigoArea, String numero, String descripcion, TelephoneType tipo) {
        this.prefijo = prefijo;
        this.codigoArea = codigoArea;
        this.numero = numero;
        this.descripcion = descripcion;
        this.tipo = tipo;
    }

}

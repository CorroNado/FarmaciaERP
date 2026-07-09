package FarmaciaERP.infrastucture.persistence.entities;

import FarmaciaERP.domain.enums.AddressLabel;
import FarmaciaERP.domain.enums.AddressStatus;
import FarmaciaERP.domain.enums.OwnerType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Address")
public class AddressJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="address_id")
    private Long id;
    @Column(name = "owner_id", nullable = false)
    private Long dueñoId;

    @Enumerated(EnumType.STRING)
    @Column(name = "owner_type", nullable = false)
    private OwnerType tipoDueño;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id", nullable = false)
    private DistrictJPA distrito;

    @Column(name = "description", nullable = false, length = 255)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "label", nullable = false)
    private AddressLabel etiqueta;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AddressStatus estado;

    protected AddressJPA() {
    }

    public AddressJPA(Long dueñoId, OwnerType tipo, DistrictJPA distrito, String descripcion, AddressLabel etiqueta, AddressStatus estado) {
        this.dueñoId = dueñoId;
        this.tipoDueño = tipo;
        this.distrito = distrito;
        this.descripcion = descripcion;
        this.etiqueta = etiqueta;
        this.estado = estado;
    }
}
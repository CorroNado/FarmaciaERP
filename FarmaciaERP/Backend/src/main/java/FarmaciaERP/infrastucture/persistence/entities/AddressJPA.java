package FarmaciaERP.infrastucture.persistence.entities;

import FarmaciaERP.domain.enums.AddressLabel;
import FarmaciaERP.domain.enums.AddressStatus;
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
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserJPA usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id", nullable = false)
    private DistrictJPA distrito;

    @Column(name = "descripcion", nullable = false, length = 255)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "etiqueta", nullable = false)
    private AddressLabel etiqueta;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private AddressStatus estado;

    protected AddressJPA() {
    }

    public AddressJPA(UserJPA usuario, DistrictJPA distrito, String descripcion, AddressLabel etiqueta, AddressStatus estado) {
        this.usuario = usuario;
        this.distrito = distrito;
        this.descripcion = descripcion;
        this.etiqueta = etiqueta;
        this.estado = estado;
    }
}
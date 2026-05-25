package FarmaciaERP.infrastucture.persistence.entities;

import FarmaciaERP.domain.enums.EmailLabel;
import FarmaciaERP.domain.enums.EmailStatus;
import FarmaciaERP.domain.enums.OwnerType;
import FarmaciaERP.infrastucture.persistence.embeddable.EmailAddressEmb;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Email_Contact")
@AllArgsConstructor
@Getter
@Setter
public class EmailContactJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "owner_type", nullable = false)
    private OwnerType ownerType;

    @Embedded
    private EmailAddressEmb direccion;

    @Enumerated(EnumType.STRING)
    @Column(name = "etiqueta", nullable = false)
    private EmailLabel etiqueta;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EmailStatus estado;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime fechaCreacion;

    protected EmailContactJPA() {
    }

    public EmailContactJPA(Long ownerId, OwnerType ownerType, EmailAddressEmb direccion, EmailLabel etiqueta, EmailStatus estado, LocalDateTime fechaCreacion) {
        this.ownerId = ownerId;
        this.ownerType = ownerType;
        this.direccion = direccion;
        this.etiqueta = etiqueta;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
    }
}

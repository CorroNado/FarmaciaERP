package FarmaciaERP.domain.entities;


import FarmaciaERP.domain.enums.EmailLabel;
import FarmaciaERP.domain.enums.EmailStatus;
import FarmaciaERP.domain.enums.OwnerType;
import FarmaciaERP.domain.valueObjects.EmailAddress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmailContact {
    private Long  id;
    private Long ownerId;
    private OwnerType ownerType;
    private EmailAddress direccion;
    private EmailLabel etiqueta;
    private EmailStatus estado;
    private LocalDateTime fechaCreacion;

    public EmailContact(EmailAddress direccion, EmailLabel etiqueta) {
        this.direccion = direccion;
        this.etiqueta = etiqueta;
        this.estado = EmailStatus.ACTIVO;
        this.fechaCreacion = LocalDateTime.now();
    }

    public void update(EmailAddress address, EmailLabel label) {
        if (this.estado == EmailStatus.INACTIVO)
            throw new IllegalArgumentException("No puedes modificar un email inactivo");
        this.direccion = address;
        this.etiqueta = label;
    }
    public void deactivate() {
        if (this.estado == EmailStatus.PRINCIPAL)
            throw new IllegalArgumentException("No puedes desactivar el email principal");
        this.estado = EmailStatus.INACTIVO;
    }
}
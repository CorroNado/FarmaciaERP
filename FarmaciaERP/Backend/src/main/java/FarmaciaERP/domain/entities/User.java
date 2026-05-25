package FarmaciaERP.domain.entities;
import FarmaciaERP.domain.enums.AddressLabel;
import FarmaciaERP.domain.enums.AddressStatus;
import FarmaciaERP.domain.enums.EmailLabel;
import FarmaciaERP.domain.enums.EmailStatus;
import FarmaciaERP.domain.valueObjects.*;
import FarmaciaERP.domain.valueObjects.usuario.LoginSecurity;
import FarmaciaERP.domain.valueObjects.usuario.Password;
import FarmaciaERP.domain.valueObjects.usuario.Username;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private Username username;
    private Password userPassword;
    private Profile profile;
    private FullName fullName;
    private LoginSecurity loginSecurity;
    private LocalDateTime createdAt;

    @Getter(AccessLevel.NONE)
    private List<EmailContact> emailContacts;
    @Getter(AccessLevel.NONE)
    private List<Address> addresses;
    @Getter(AccessLevel.NONE)
    private List<Telephone>  telephones;

    public User(Username username, Password userPassword,Profile profile, FullName fullName, List<Address> addresses, List<EmailContact> emailContacts, List<Telephone> telephones) {
        this.username = username;
        this.userPassword = userPassword;
        this.profile = profile;
        this.fullName = fullName;
        this.addresses = addresses;
        this.emailContacts = emailContacts;
        this.telephones = telephones;
        this.createdAt = LocalDateTime.now();
    }

    public Collection<Address> getAddresses() {
        return addresses;
    }
    public Collection<EmailContact> getEmailContacts() {
        return emailContacts;
    }
    public Collection<Telephone> getTelephones() {
        return telephones;
    }

    //DIRECCIONES
    public void addAddress(Address address) {
        if (this.addresses.isEmpty()) {
            address.setEstado(AddressStatus.PRINCIPAL);
        } else {
            address.setEstado(AddressStatus.ACTIVA);
        }
        this.addresses.add(address);
    }
    public void updateAddress(Long addressId, String description,
                              AddressLabel label, Long districtId) {
        Address address = this.addresses.stream()
                .filter(d -> d.getId().equals(addressId)
                        && d.getEstado() != AddressStatus.INACTIVA)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Dirección no encontrada o inactiva"));

        address.setDescripcion(description);
        address.setEtiqueta(label);
        address.setDistrictId(districtId);
    }
    public void changePrincipalAddress(Long addressId) {
        boolean exists = this.addresses.stream()
                .anyMatch(d -> d.getId().equals(addressId)
                        && d.getEstado() != AddressStatus.INACTIVA);
        if (!exists) throw new IllegalArgumentException("Dirección no encontrada o inactiva");

        this.addresses.stream()
                .filter(d -> d.getEstado() == AddressStatus.PRINCIPAL)
                .findFirst()
                .ifPresent(d -> d.setEstado(AddressStatus.ACTIVA));

        this.addresses.stream()
                .filter(d -> d.getId().equals(addressId))
                .findFirst()
                .ifPresent(d -> d.setEstado(AddressStatus.PRINCIPAL));
    }
    public void removeAddress(Long addressId) {
        Address address = this.addresses.stream()
                .filter(d -> d.getId().equals(addressId)
                        && d.getEstado() != AddressStatus.INACTIVA)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Dirección no encontrada o ya inactiva"));

        if (address.getEstado() == AddressStatus.PRINCIPAL)
            throw new IllegalArgumentException("No puedes desactivar la dirección principal");

        address.setEstado(AddressStatus.INACTIVA);
    }

    // TELEFONOS
    public void addTelephone(Telephone telephone) {
        this.telephones.add(telephone);
    }
    public void removeTelephone(Telephone telephone) {
        this.telephones.remove(telephone);
    }
    public void updateTelephone(Telephone old, Telephone updated) {
        removeTelephone(old);
        addTelephone(updated);
    }

    //EMAILS
    public void addEmail(EmailContact emailContact) {
        if (this.emailContacts.isEmpty()) {
            emailContact.setEstado(EmailStatus.PRINCIPAL);
        } else {
            emailContact.setEstado(EmailStatus.ACTIVO);
        }
        this.emailContacts.add(emailContact);
    }
    public void updateEmail(Long emailId, EmailAddress address, EmailLabel label) {
        emailContacts.stream()
                .filter(e -> e.getId().equals(emailId)
                        && e.getEstado() != EmailStatus.INACTIVO)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("EmailContact no encontrada o inactiva"))
                .update(address,label);
    }

    public void removeEmail(Long emailId) {
        this.emailContacts.stream()
                .filter(e -> e.getId().equals(emailId)
                        && e.getEstado() != EmailStatus.INACTIVO)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("EmailContact no encontrado o inactivo"))
                .deactivate();
    }
    public void changePrincipalEmail(Long emailId) {
        this.emailContacts.stream()
                .filter(e -> e.getEstado() == EmailStatus.PRINCIPAL)
                .findFirst()
                .ifPresent(e -> e.setEstado(EmailStatus.ACTIVO));

        this.emailContacts.stream()
                .filter(e -> e.getId().equals(emailId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("EmailContact no encontrado"))
                .setEstado(EmailStatus.PRINCIPAL);
    }

}
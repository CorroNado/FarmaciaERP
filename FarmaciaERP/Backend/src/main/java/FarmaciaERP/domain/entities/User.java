package FarmaciaERP.domain.entities;
import FarmaciaERP.domain.enums.*;
import FarmaciaERP.domain.valueObjects.*;
import FarmaciaERP.domain.valueObjects.usuario.LoginSecurity;
import FarmaciaERP.domain.valueObjects.usuario.Password;
import FarmaciaERP.domain.valueObjects.usuario.Username;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private Long perfilId;
    private FullName nombreCompleto;
    private LoginSecurity loginSeguro;
    private LocalDateTime fechaCreacion;

    @Getter(AccessLevel.NONE)
    private List<EmailContact> emailContacts;
    @Getter(AccessLevel.NONE)
    private List<Address> direcciones;
    @Getter(AccessLevel.NONE)
    private List<Telephone>  telefonos;

    public User(FullName nombreCompleto, Long perfilId, Password userPassword, Username username) {
        this.username = username;
        this.userPassword = userPassword;
        this.perfilId = perfilId;
        this.nombreCompleto = nombreCompleto;
        this.loginSeguro = new LoginSecurity();
        this.fechaCreacion = LocalDateTime.now();
        this.emailContacts = new ArrayList<>();
        this.direcciones = new ArrayList<>();
        this.telefonos = new ArrayList<>();
    }

    public User(Username username, Password userPassword, Long perfilId, FullName nombreCompleto, LoginSecurity loginSeguro, LocalDateTime fechaCreacion, List<EmailContact> emailContacts, List<Address> direcciones, List<Telephone> telefonos) {
        this.username = username;
        this.userPassword = userPassword;
        this.perfilId = perfilId;
        this.nombreCompleto = nombreCompleto;
        this.loginSeguro = loginSeguro;
        this.fechaCreacion = fechaCreacion;
        this.emailContacts = emailContacts;
        this.direcciones = direcciones;
        this.telefonos = telefonos;
    }

    public Collection<Address> getDirecciones() {
        return direcciones;
    }
    public Collection<EmailContact> getEmailContacts() {
        return emailContacts;
    }
    public Collection<Telephone> getTelefonos() {
        return telefonos;
    }

    public void desactivar() {
        this.setLoginSeguro(new LoginSecurity(UserStatus.ACTIVO));
    }
    //DIRECCIONES
    public EmailContact getEmailPrincipal(){
        EmailContact principal = emailContacts.stream()
                .filter(e -> e.getEstado().equals(EmailStatus.PRINCIPAL))
                .findFirst().orElseThrow(()-> new RuntimeException("SI"));
        return principal;
    }

    public void añadirDireccion(Address direccion) {
        if (this.direcciones.isEmpty()) {
            direccion.setEstado(AddressStatus.PRINCIPAL);
        } else {
            direccion.setEstado(AddressStatus.ACTIVA);
        }
        this.direcciones.add(direccion);
    }
    public void actualizarDireccion(Long direccionesId, String descripcion,
                              AddressLabel etiqueta, Long distritoId) {
        Address address = this.direcciones.stream()
                .filter(d -> d.getId().equals(direccionesId)
                        && d.getEstado() != AddressStatus.INACTIVA)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Dirección no encontrada o inactiva"));

        address.setDescripcion(descripcion);
        address.setEtiqueta(etiqueta);
        address.setDistrictId(distritoId);
    }
    public void cambiarPrincipalDireccion(Long direccionId) {
        boolean exists = this.direcciones.stream()
                .anyMatch(d -> d.getId().equals(direccionId)
                        && d.getEstado() != AddressStatus.INACTIVA);
        if (!exists) throw new IllegalArgumentException("Dirección no encontrada o inactiva");

        this.direcciones.stream()
                .filter(d -> d.getEstado() == AddressStatus.PRINCIPAL)
                .findFirst()
                .ifPresent(d -> d.setEstado(AddressStatus.ACTIVA));

        this.direcciones.stream()
                .filter(d -> d.getId().equals(direccionId))
                .findFirst()
                .ifPresent(d -> d.setEstado(AddressStatus.PRINCIPAL));
    }
    public void eliminarDireccion(Long direccionId) {
        Address address = this.direcciones.stream()
                .filter(d -> d.getId().equals(direccionId)
                        && d.getEstado() != AddressStatus.INACTIVA)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Dirección no encontrada o ya inactiva"));

        if (address.getEstado() == AddressStatus.PRINCIPAL)
            throw new IllegalArgumentException("No puedes desactivar la dirección principal");

        address.setEstado(AddressStatus.INACTIVA);
    }

    // TELEFONOS
    public void añadirTelefono(Telephone telefono) {
        this.telefonos.add(telefono);
    }
    public void eliminarTelefono(Telephone telefono) { this.telefonos.remove(telefono);}
    public void actualizarTelefono(Telephone antiguo, Telephone nuevo) {
        eliminarTelefono(antiguo);
        añadirTelefono(nuevo);
    }

    //EMAILS
    public EmailContact añadirEmail(EmailContact emailContact) {
        if (this.emailContacts.isEmpty()) {
            emailContact.setEstado(EmailStatus.PRINCIPAL);
        } else {
            emailContact.setEstado(EmailStatus.ACTIVO);
        }
        this.emailContacts.add(emailContact);
        return emailContact;
    }
    public void actualizarEmail(Long emailId, EmailAddress address, EmailLabel label) {
        emailContacts.stream()
                .filter(e -> e.getId().equals(emailId)
                        && e.getEstado() != EmailStatus.INACTIVO)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("EmailContact no encontrada o inactiva"))
                .update(address,label);
    }

    public void eliminarEmail(Long emailId) {
        this.emailContacts.stream()
                .filter(e -> e.getId().equals(emailId)
                        && e.getEstado() != EmailStatus.INACTIVO)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("EmailContact no encontrado o inactivo"))
                .deactivate();
    }
    public void cambiarPrincipalEmail(Long emailId) {
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
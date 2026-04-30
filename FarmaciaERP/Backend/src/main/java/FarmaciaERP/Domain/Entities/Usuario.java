package FarmaciaERP.Domain.Entities;
import FarmaciaERP.Domain.Enums.RolUsuario;
import FarmaciaERP.Domain.Enums.UsuarioEstados;
import FarmaciaERP.Domain.ValueObjects.Email;
import FarmaciaERP.Domain.ValueObjects.FullName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class Usuario{
    private Long id;
    private FullName nombres;
    private Email email;
    private String constrasena;
    private RolUsuario role;
    private UsuarioEstados estado;
    private LocalDateTime registro;

    public Usuario() {
    }

    public Usuario(FullName nombres, Email email, String constrasena, RolUsuario rol) {
        this.nombres = nombres;
        this.email = email;
        this.constrasena = constrasena;
        this.role = rol;
        this.estado = UsuarioEstados.ACTIVO;
        this.registro = LocalDateTime.now();
    }

    public Usuario(FullName nombres, Email email, String constrasena, RolUsuario rol, UsuarioEstados estado, LocalDateTime registro) {
        this.nombres = nombres;
        this.email = email;
        this.constrasena = constrasena;
        this.role = rol;
        this.estado = estado;
        this.registro = registro;
    }
}
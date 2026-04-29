package FarmaciaERP.Domain.Entities;
import FarmaciaERP.Domain.Enums.UsuarioEstados;
import FarmaciaERP.Domain.ValueObjects.FullName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class Usuario{
    private int id;
    private FullName nombres;
    private String email;
    private String password;
    private UsuarioEstados estado;
    private LocalDateTime registro;

    public Usuario() {
    }

    public Usuario(FullName nombres, String email, String password) {
        this.nombres = nombres;
        this.email = email;
        this.password = password;
        this.estado = UsuarioEstados.ACTIVO;
        this.registro = LocalDateTime.now();
    }

    public Usuario(FullName nombres, String email, String password, UsuarioEstados estado, LocalDateTime registro) {
        this.nombres = nombres;
        this.email = email;
        this.password = password;
        this.estado = estado;
        this.registro = registro;
    }
}
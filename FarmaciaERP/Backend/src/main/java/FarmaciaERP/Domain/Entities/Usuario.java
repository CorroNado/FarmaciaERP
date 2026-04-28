package FarmaciaERP.Domain.Entities;
import FarmaciaERP.Domain.Enums.UsuarioEstados;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Usuario{
    private int id;
    private String nombre;
    private String email;
    private String password;
    private UsuarioEstados estado;
    private LocalDateTime registro;

    public Usuario() {
    }

    public Usuario(String nombre, String email, String password) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.estado = UsuarioEstados.ACTIVO;
        this.registro = LocalDateTime.now();
    }


}
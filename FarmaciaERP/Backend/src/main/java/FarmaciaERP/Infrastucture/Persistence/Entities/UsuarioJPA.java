package FarmaciaERP.Infrastucture.Persistence.Entities;

import FarmaciaERP.Domain.Enums.UsuarioEstados;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "Usuarios")
@Getter
@Setter
public class UsuarioJPA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UsuarioEstados estado;

    @Column(name = "fecha_registro", updatable = false)
    private LocalDateTime registro;

    public UsuarioJPA() {
    }

    public UsuarioJPA(int id, String nombre, String email, String password, UsuarioEstados estado, LocalDateTime registro) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.estado = estado;
        this.registro = registro;
    }
}

package FarmaciaERP.Infrastucture.Persistence.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

    @Column(nullable = false, length = 20)
    private String estado;

    @Column(name = "fecha_registro", updatable = false)
    private LocalDateTime registro;

    public UsuarioJPA() {
    }

    public UsuarioJPA(int id, String nombre, String email, String password, String estado, LocalDateTime registro) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.estado = estado;
        this.registro = registro;
    }
}

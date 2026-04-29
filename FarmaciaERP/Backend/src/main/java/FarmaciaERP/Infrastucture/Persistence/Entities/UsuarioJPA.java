package FarmaciaERP.Infrastucture.Persistence.Entities;

import FarmaciaERP.Domain.Enums.UsuarioEstados;
import FarmaciaERP.Domain.ValueObjects.Email;
import FarmaciaERP.Infrastucture.Persistence.ValueObjects.EmailEmb;
import FarmaciaERP.Infrastucture.Persistence.ValueObjects.FullNameEmb;
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

    @Embedded
    private FullNameEmb nombres;

    @Embedded
    private EmailEmb email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UsuarioEstados estado;

    @Column(name = "fecha_registro", updatable = false)
    private LocalDateTime registro;

    public UsuarioJPA() {
    }

    public UsuarioJPA(int id, FullNameEmb nombres, EmailEmb email, String password, UsuarioEstados estado, LocalDateTime registro) {
        this.id = id;
        this.nombres = nombres;
        this.email = email;
        this.password = password;
        this.estado = estado;
        this.registro = registro;
    }
}

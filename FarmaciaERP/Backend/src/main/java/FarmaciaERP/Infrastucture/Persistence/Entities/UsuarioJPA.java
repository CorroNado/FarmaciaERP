package FarmaciaERP.Infrastucture.Persistence.Entities;

import FarmaciaERP.Domain.Enums.RolUsuario;
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
    private Long id;

    @Embedded
    private FullNameEmb nombres;

    @Embedded
    private EmailEmb email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private RolUsuario rol;

    @Enumerated(EnumType.STRING)
    private UsuarioEstados estado;

    @Column(name = "fecha_registro", updatable = false)
    private LocalDateTime registro;

    public UsuarioJPA() {
    }

    public UsuarioJPA(Long id, FullNameEmb nombres, EmailEmb email, String password, RolUsuario rol, UsuarioEstados estado, LocalDateTime registro) {
        this.id = id;
        this.nombres = nombres;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.estado = estado;
        this.registro = registro;
    }

    public String getEmailValue() {
        return email.getEmail();
    }
    public String getNombresValue() {
        return nombres.getNombres();
    }
    public String getApellidosValue() {
        return nombres.getApellidos();
    }
}

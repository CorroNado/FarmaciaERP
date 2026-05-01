package FarmaciaERP.Infrastucture.Persistence.Entities;

import FarmaciaERP.Infrastucture.Persistence.ValueObjects.EmailEmb;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "historial_acceso")
public class HistorialAccesoJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioJPA usuario;

    @Embedded
    private EmailEmb email;

    @Column(nullable = false)
    private String accion;

    @Column(nullable = false)
    private String ip;

    @Column(nullable = false)
    private LocalDateTime fecha;

    public HistorialAccesoJPA() {}

    public HistorialAccesoJPA(UsuarioJPA usuario, EmailEmb email, String accion, String ip) {
        this.usuario = usuario;
        this.email = email;
        this.accion = accion;
        this.ip = ip;
        this.fecha = LocalDateTime.now();
    }

}
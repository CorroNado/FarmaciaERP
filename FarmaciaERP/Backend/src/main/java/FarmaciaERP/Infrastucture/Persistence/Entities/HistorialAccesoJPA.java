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

    @Column(nullable = false,length = 100)
    private String email;

    @Column(nullable = false,length = 200)
    private String accion;

    @Column(nullable = false,length = 45)
    private String ip;

    @Column(nullable = false)
    private LocalDateTime fecha;

    public HistorialAccesoJPA() {}

    public HistorialAccesoJPA(UsuarioJPA usuario, String email, String accion, String ip) {
        this.usuario = usuario;
        this.email = email;
        this.accion = accion;
        this.ip = ip;
        this.fecha = LocalDateTime.now();
    }

}
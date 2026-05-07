package FarmaciaERP.Infrastucture.Persistence.Entities;

import FarmaciaERP.Domain.Enums.AccionAcceso;
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
    private String userAgent;

    @Enumerated(EnumType.STRING)
    private AccionAcceso accion;

    @Column(nullable = false,length = 45)
    private String ip;

    @Column(nullable = false)
    private LocalDateTime fecha;

    public HistorialAccesoJPA() {}

    public HistorialAccesoJPA(UsuarioJPA usuario, AccionAcceso accion, String ip,String userAgent) {
        this.usuario = usuario;
        this.userAgent = userAgent;
        this.accion = accion;
        this.ip = ip;
        this.fecha = LocalDateTime.now();
    }

}
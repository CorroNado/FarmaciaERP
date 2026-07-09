package FarmaciaERP.infrastucture.persistence.entities;

import FarmaciaERP.domain.enums.LoginAction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "Login_History")
public class LoginHistoryJPA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loginHistorialId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserJPA usuario;

    @Column(nullable = false)
    private String userAgent;

    @Enumerated(EnumType.STRING)
    private LoginAction accion;

    @Column(nullable = false,length = 45)
    private String ip;

    @Column(nullable = false)
    private LocalDateTime fecha;

    protected LoginHistoryJPA() {}

    public LoginHistoryJPA(UserJPA usuario, LoginAction accion, String ip, String userAgent) {
        this.usuario = usuario;
        this.userAgent = userAgent;
        this.accion = accion;
        this.ip = ip;
        this.fecha = LocalDateTime.now();
    }

}
package FarmaciaERP.Infrastucture.Persistence.Entities;

import FarmaciaERP.Domain.Enums.EstadoPropuestaPago;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "PropuestaPagoAutomatica")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropuestaPagoAutomaticaJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numero;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lote_pago_tesoreria_id", nullable = false, unique = true)
    private LotePagoTesoreriaJPA lotePagoTesoreria;

    @Column
    private String sociedad;

    @Column(name = "via_pago")
    private String viaPago;

    @Column(name = "fecha_pago")
    private String fechaPago;

    @Column(name = "parametros_introducidos", nullable = false)
    private boolean parametrosIntroducidos;

    @Column(name = "propuesta_ejecutada", nullable = false)
    private boolean propuestaEjecutada;

    @Column(name = "monto_propuesta", nullable = false)
    private double montoPropuesta;

    @Column(nullable = false)
    private int intentos;

    @Column(name = "propuesta_correcta")
    private Boolean propuestaCorrecta;

    @Column(name = "propuesta_aprobada", nullable = false)
    private boolean propuestaAprobada;

    @Column(name = "pago_ejecutado", nullable = false)
    private boolean pagoEjecutado;

    @Column(name = "archivos_generados", nullable = false)
    private boolean archivosGenerados;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPropuestaPago estado;

    @Column(nullable = false)
    private LocalDateTime fecha;
}

package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Enums.EstadoOrdenTraslado;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * LOG.07 Orden de Traslado (STO, ME27/MB1B) - Fase 06: gestión de stocks y
 * distribución capilar. El lote liberado por QA (Decisión de Empleo
 * aprobada, Fase 05) dispara el algoritmo Push/Pull, se genera la STO con
 * su guía de remisión, y el inventario queda "en tránsito" hasta que la
 * sucursal confirma la recepción en el POS.
 */
@Getter
@Setter
public class OrdenTraslado {

    private Long id;
    private String numero;
    private InspeccionCalidad inspeccionCalidad;
    private Sucursal sucursalDestino;
    private String guiaRemision;
    private EstadoOrdenTraslado estado;
    private LocalDateTime fechaDespacho;
    private LocalDateTime fechaRecepcion;

    private OrdenTraslado() {
    }

    /**
     * RN-E6-002 · RN-E6-004 · RN-E6-005 · RN-E6-007 · RN-E6-008 - Genera y
     * despacha la STO. Solo procede sobre un lote con Decisión de Empleo
     * aprobada (Libre Utilización); el stock trasladado queda "en tránsito"
     * y no disponible para venta hasta la confirmación en POS (RN-E6-009).
     */
    public static OrdenTraslado generar(String numero, InspeccionCalidad inspeccionCalidad,
                                         Sucursal sucursalDestino, String guiaRemision) {
        if (numero == null || numero.isBlank()) {
            throw new BadRequestException("El número de la Orden de Traslado (STO) es obligatorio");
        }
        if (inspeccionCalidad == null) {
            throw new BadRequestException(
                    "La Orden de Traslado debe originarse de un lote con Decisión de Empleo (QA11)");
        }
        // RN-E6-002: no se puede distribuir un lote sin Decisión de Empleo aprobada.
        if (!inspeccionCalidad.puedeDistribuir()) {
            throw new BadRequestException(
                    "RN-E6-002: no se puede distribuir. El lote no tiene Decisión de Empleo aprobada (QA11).");
        }
        if (sucursalDestino == null) {
            throw new BadRequestException("RN-E6-005: la sucursal destino es obligatoria");
        }
        if (!sucursalDestino.isActiva()) {
            throw new BadRequestException("RN-E6-005: la sucursal destino no está activa");
        }
        if (guiaRemision == null || guiaRemision.isBlank()) {
            throw new BadRequestException("RN-E6-007: la guía de remisión es obligatoria");
        }

        OrdenTraslado orden = new OrdenTraslado();
        orden.numero = numero;
        orden.inspeccionCalidad = inspeccionCalidad;
        orden.sucursalDestino = sucursalDestino;
        orden.guiaRemision = guiaRemision;
        // RN-E6-008: mientras la sucursal no confirme recepción, el stock permanece en tránsito.
        orden.estado = EstadoOrdenTraslado.EN_TRANSITO;
        orden.fechaDespacho = LocalDateTime.now();
        return orden;
    }

    /**
     * RN-E6-009 - Confirma la recepción en el POS de la sucursal destino:
     * el stock deja de estar en tránsito y queda disponible para la venta.
     */
    public void confirmarRecepcion() {
        if (estado == EstadoOrdenTraslado.RECIBIDO) {
            throw new BadRequestException("La Orden de Traslado " + numero + " ya tiene recepción confirmada");
        }
        this.estado = EstadoOrdenTraslado.RECIBIDO;
        this.fechaRecepcion = LocalDateTime.now();
    }

    public boolean estaEnTransito() {
        return estado == EstadoOrdenTraslado.EN_TRANSITO;
    }

    public static OrdenTraslado reconstruir(Long id, String numero, InspeccionCalidad inspeccionCalidad,
                                             Sucursal sucursalDestino, String guiaRemision,
                                             EstadoOrdenTraslado estado, LocalDateTime fechaDespacho,
                                             LocalDateTime fechaRecepcion) {
        OrdenTraslado orden = new OrdenTraslado();
        orden.id = id;
        orden.numero = numero;
        orden.inspeccionCalidad = inspeccionCalidad;
        orden.sucursalDestino = sucursalDestino;
        orden.guiaRemision = guiaRemision;
        orden.estado = estado;
        orden.fechaDespacho = fechaDespacho;
        orden.fechaRecepcion = fechaRecepcion;
        return orden;
    }
}

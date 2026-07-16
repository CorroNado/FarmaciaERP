package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Enums.EstadoSolPed;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * LOG.01 Solicitud de Pedido (SolPed / PR) - Fase 01: Planificación de
 * necesidades (MRP). Punto de partida del ciclo Procure-to-Pay.
 */
@Getter
@Setter
public class SolicitudPedido {
    private Long id;
    private String numero;
    private LocalDateTime fecha;
    private String responsable;
    private String centroCosto;
    private double presupuesto;
    private List<DetalleSolPed> detalles = new ArrayList<>();
    private EstadoSolPed estado;
    private Proveedor proveedor;
    private Convenio convenio;
    private String motivoRechazo;

    public SolicitudPedido() {
    }

    public SolicitudPedido(String numero, String responsable, String centroCosto, double presupuesto,
                            List<DetalleSolPed> detalles) {
        if (numero == null || numero.isBlank()) {
            throw new BadRequestException("El número de SolPed es obligatorio");
        }
        if (responsable == null || responsable.isBlank()) {
            throw new BadRequestException("La SolPed debe indicar un responsable (Category Manager)");
        }
        // RN-E1-004: toda necesidad debe estar respaldada por la planificación MRP.
        if (detalles == null || detalles.isEmpty()) {
            throw new BadRequestException("RN-E1-004: la SolPed debe tener al menos un ítem respaldado por MRP");
        }
        this.numero = numero;
        this.responsable = responsable;
        this.centroCosto = centroCosto;
        this.presupuesto = presupuesto;
        this.detalles = new ArrayList<>(detalles);
        this.fecha = LocalDateTime.now();
        // RN-E1-010: la SolPed se libera automáticamente a Fase 02 al crearse.
        this.estado = EstadoSolPed.LIBERADA;
    }

    public double getTotal() {
        return detalles.stream().mapToDouble(DetalleSolPed::getSubtotal).sum();
    }

    /**
     * Fase 02 (RN-MM-001, RN-MM-004, RN-MM-005) - Se aprueba la fuente de
     * aprovisionamiento tras validar convenio vigente y presupuesto disponible.
     */
    public void aprobarFuente(Proveedor proveedor, Convenio convenio) {
        cambiarEstado(EstadoSolPed.FUENTE_APROBADA);
        if (!convenio.estaVigente()) {
            throw new BadRequestException("RN-MM-001: el convenio marco no está vigente");
        }
        if (getTotal() > presupuesto) {
            throw new BadRequestException(
                    "RN-MM-005: fondos insuficientes en el Centro de Costo " + centroCosto);
        }
        this.proveedor = proveedor;
        this.convenio = convenio;
    }

    public void convertirEnOrdenCompra() {
        cambiarEstado(EstadoSolPed.CONVERTIDA_OC);
    }

    public void rechazar(String motivo) {
        if (motivo == null || motivo.isBlank()) {
            throw new BadRequestException("Debe indicar el motivo de rechazo de la SolPed");
        }
        cambiarEstado(EstadoSolPed.RECHAZADA);
        this.motivoRechazo = motivo;
    }

    private void cambiarEstado(EstadoSolPed nuevoEstado) {
        if (!estado.puedeCambiarA(nuevoEstado)) {
            throw new BadRequestException("No se puede cambiar el estado de " + estado + " a " + nuevoEstado);
        }
        this.estado = nuevoEstado;
    }
}

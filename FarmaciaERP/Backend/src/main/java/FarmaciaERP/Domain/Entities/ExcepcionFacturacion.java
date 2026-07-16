package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Enums.EstadoExcepcionFacturacion;
import FarmaciaERP.Domain.Enums.ResultadoConciliacion;
import FarmaciaERP.Domain.Enums.TipoDiscrepancia;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * FI-AP.01 Captura de Excepciones de Facturación - Fase 01 (Frontera
 * Logística): el sistema bloquea automáticamente toda factura de proveedor
 * cuya conciliación de 3 vías (MRBR) haya resultado en desviación frente al
 * vademécum de precios o a la cantidad recibida. El Analista de Cuentas por
 * Pagar revisa cada excepción y clasifica el tipo de discrepancia antes de
 * derivarla a Compras / Category Manager (Fase 02).
 */
@Getter
@Setter
public class ExcepcionFacturacion {

    private Long id;
    private String numero;
    private ConciliacionTresVias conciliacionTresVias;
    private double montoFactura;
    private double montoContrato;
    private double diferencia;
    private TipoDiscrepancia tipoDiscrepancia;
    private EstadoExcepcionFacturacion estado;
    private boolean revisada;
    private boolean clasificada;
    private boolean notificada;
    private LocalDateTime fecha;

    private ExcepcionFacturacion() {
    }

    /**
     * RN-AP1-01: sólo puede capturarse una excepción de facturación a partir
     * de una conciliación de 3 vías (MRBR) cuyo resultado sea
     * BLOQUEADO_MRBR. La factura queda bloqueada en excepción hasta que el
     * Analista de Cuentas por Pagar la revise y clasifique.
     */
    public static ExcepcionFacturacion capturar(String numero, ConciliacionTresVias conciliacionTresVias) {
        if (numero == null || numero.isBlank()) {
            throw new BadRequestException("El número de la excepción de facturación es obligatorio");
        }
        if (conciliacionTresVias == null) {
            throw new BadRequestException(
                    "RN-AP1-01: la excepción de facturación debe originarse de una conciliación de 3 vías (MRBR)");
        }
        if (conciliacionTresVias.getResultado() != ResultadoConciliacion.BLOQUEADO_MRBR) {
            throw new BadRequestException(
                    "RN-AP1-01: sólo se capturan excepciones para conciliaciones bloqueadas en MRBR"
                            + " (la conciliación " + conciliacionTresVias.getNumero() + " tuvo match exitoso)");
        }

        ExcepcionFacturacion excepcion = new ExcepcionFacturacion();
        excepcion.numero = numero;
        excepcion.conciliacionTresVias = conciliacionTresVias;

        // RN-AP1-02: el monto facturado (MIRO) se contrasta contra el monto
        // pactado en la Orden de Compra para determinar la desviación.
        excepcion.montoFactura = conciliacionTresVias.getFacturaMIRO().getMontoTotal();
        excepcion.montoContrato = conciliacionTresVias.getOrdenCompra().getMontoTotal();
        excepcion.diferencia = excepcion.montoFactura - excepcion.montoContrato;

        excepcion.estado = EstadoExcepcionFacturacion.BLOQUEADA;
        excepcion.revisada = false;
        excepcion.clasificada = false;
        excepcion.notificada = false;
        excepcion.fecha = LocalDateTime.now();
        return excepcion;
    }

    /**
     * RN-AP1-03: paso 1.1 — el Analista de Cuentas por Pagar revisa el panel
     * de facturas bloqueadas antes de poder analizar la discrepancia.
     */
    public void revisar() {
        if (estado != EstadoExcepcionFacturacion.BLOQUEADA) {
            throw new BadRequestException(
                    "RN-AP1-03: la excepción " + numero + " ya fue revisada");
        }
        this.revisada = true;
        this.estado = EstadoExcepcionFacturacion.REVISADA;
    }

    /**
     * RN-AP1-04: paso 1.2 — el Analista clasifica la discrepancia como
     * Precio o Cantidad. Al clasificarla, el sistema dispara automáticamente
     * (paso 1.3) la notificación interna a Compras / Category Manager.
     */
    public void clasificar(TipoDiscrepancia tipoDiscrepancia) {
        if (!revisada) {
            throw new BadRequestException(
                    "RN-AP1-04: debe revisarse el panel de facturas bloqueadas antes de clasificar la discrepancia");
        }
        if (estado == EstadoExcepcionFacturacion.NOTIFICADA) {
            throw new BadRequestException(
                    "RN-AP1-04: la excepción " + numero + " ya fue clasificada y notificada");
        }
        if (tipoDiscrepancia == null) {
            throw new BadRequestException("RN-AP1-04: el tipo de discrepancia es obligatorio (Precio o Cantidad)");
        }

        this.tipoDiscrepancia = tipoDiscrepancia;
        this.clasificada = true;
        // RN-AP1-05: notificación interna automática a Compras al clasificar.
        this.notificada = true;
        this.estado = EstadoExcepcionFacturacion.NOTIFICADA;
    }

    public boolean estaNotificada() {
        return estado == EstadoExcepcionFacturacion.NOTIFICADA;
    }

    /**
     * Reconstrucción desde persistencia (usada por el mapper).
     */
    public static ExcepcionFacturacion reconstruir(Long id, String numero, ConciliacionTresVias conciliacionTresVias,
                                                     double montoFactura, double montoContrato, double diferencia,
                                                     TipoDiscrepancia tipoDiscrepancia, EstadoExcepcionFacturacion estado,
                                                     boolean revisada, boolean clasificada, boolean notificada,
                                                     LocalDateTime fecha) {
        ExcepcionFacturacion excepcion = new ExcepcionFacturacion();
        excepcion.id = id;
        excepcion.numero = numero;
        excepcion.conciliacionTresVias = conciliacionTresVias;
        excepcion.montoFactura = montoFactura;
        excepcion.montoContrato = montoContrato;
        excepcion.diferencia = diferencia;
        excepcion.tipoDiscrepancia = tipoDiscrepancia;
        excepcion.estado = estado;
        excepcion.revisada = revisada;
        excepcion.clasificada = clasificada;
        excepcion.notificada = notificada;
        excepcion.fecha = fecha;
        return excepcion;
    }
}

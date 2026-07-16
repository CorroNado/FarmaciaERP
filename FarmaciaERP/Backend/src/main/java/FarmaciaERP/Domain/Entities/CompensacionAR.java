package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Enums.EstadoCompensacionAR;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * FI-AR · Fase 06 — Compensación Final y Análisis de Margen Neto
 * (RN-AR6-01). Cierre del ciclo FI-AR del período: Tesorería aplica la
 * compensación automática sobre la cuenta del cliente, se genera el
 * Reporte de Rendimiento Comercial y Margen de la Cadena, se confirma
 * el saldo limpio en cuentas corrientes y, finalmente, se cierran los
 * ingresos por convenio.
 */
@Getter
@Setter
public class CompensacionAR {

    private Long id;
    private ContabilizacionAR contabilizacionAR;
    private boolean compensado;
    private boolean reporteGenerado;
    private double montoVentas;
    private double montoAprobadas;
    private double perdidas;
    private double margenNeto;
    private double margenPct;
    private boolean saldoConfirmado;
    private boolean cerrado;
    private EstadoCompensacionAR estado;
    private LocalDateTime fecha;
    private LocalDateTime fechaCierre;

    private CompensacionAR() {
    }

    /**
     * 6.1 - Aplicar Compensación Automática sobre la Cuenta del Cliente
     * (Tesorería / Finanzas). RN-AR6-01: solo puede aplicarse cuando la
     * Fase 05 ya registró el ingreso de dinero e imputación bancaria.
     */
    public static CompensacionAR aplicarCompensacionAutomatica(ContabilizacionAR contabilizacionAR) {
        if (contabilizacionAR == null) {
            throw new BadRequestException(
                    "La compensación final debe originarse del lote de la Fase 02 (Contabilización AR)");
        }

        CompensacionAR compensacion = new CompensacionAR();
        compensacion.contabilizacionAR = contabilizacionAR;
        compensacion.compensado = true;
        compensacion.estado = EstadoCompensacionAR.COMPENSADO;
        compensacion.fecha = LocalDateTime.now();
        return compensacion;
    }

    /**
     * Generar Reporte de Rendimiento Comercial y Margen de la Cadena.
     * RN-AR6-01: el margen neto del período resulta de las ventas del
     * día (POS) menos las pérdidas por débitos no subsanables (ajustes
     * técnicos contables de la Fase 04).
     */
    public void generarReporteRendimiento(double montoVentas, double montoAprobadas, double perdidas) {
        if (!this.compensado) {
            throw new BadRequestException(
                    "RN-AR6-01: debe aplicarse la compensación automática antes de generar el reporte de rendimiento");
        }
        if (this.reporteGenerado) {
            throw new BadRequestException("El reporte de rendimiento comercial ya fue generado");
        }

        this.montoVentas = montoVentas;
        this.montoAprobadas = montoAprobadas;
        this.perdidas = perdidas;
        this.margenNeto = Math.round((montoVentas - perdidas) * 100) / 100.0;
        this.margenPct = montoVentas > 0 ? Math.round((this.margenNeto / montoVentas) * 1000) / 10.0 : 0;
        this.reporteGenerado = true;
        this.estado = EstadoCompensacionAR.REPORTE_GENERADO;
    }

    /**
     * Confirmar Saldo Limpio en Cuentas Corrientes, previo al cierre de
     * ingresos por convenio.
     */
    public void confirmarSaldoLimpio() {
        if (!this.reporteGenerado) {
            throw new BadRequestException(
                    "RN-AR6-01: debe generarse el reporte de rendimiento antes de confirmar el saldo limpio");
        }
        if (this.saldoConfirmado) {
            throw new BadRequestException("El saldo limpio en cuentas corrientes ya fue confirmado");
        }

        this.saldoConfirmado = true;
        this.estado = EstadoCompensacionAR.SALDO_CONFIRMADO;
    }

    /**
     * Cerrar Ingresos por Convenio. RN-AR6-01: paso terminal que
     * finaliza exitosamente el ciclo FI-AR del período.
     */
    public void cerrarIngresosPorConvenio() {
        if (!this.saldoConfirmado) {
            throw new BadRequestException(
                    "RN-AR6-01: debe confirmarse el saldo limpio antes de cerrar los ingresos por convenio");
        }
        if (this.cerrado) {
            throw new BadRequestException("El cierre de ingresos por convenio ya fue completado");
        }

        this.cerrado = true;
        this.estado = EstadoCompensacionAR.CERRADO;
        this.fechaCierre = LocalDateTime.now();
    }

    /**
     * RN-AR6-01: el ciclo FI-AR del período queda finalizado cuando se
     * completa el cierre de ingresos por convenio.
     */
    public boolean estaFinalizado() {
        return this.estado == EstadoCompensacionAR.CERRADO;
    }

    /**
     * Reconstrucción desde persistencia (usada por el mapper).
     */
    public static CompensacionAR reconstruir(Long id, ContabilizacionAR contabilizacionAR, boolean compensado,
                                              boolean reporteGenerado, double montoVentas, double montoAprobadas,
                                              double perdidas, double margenNeto, double margenPct,
                                              boolean saldoConfirmado, boolean cerrado,
                                              EstadoCompensacionAR estado, LocalDateTime fecha,
                                              LocalDateTime fechaCierre) {
        CompensacionAR compensacion = new CompensacionAR();
        compensacion.id = id;
        compensacion.contabilizacionAR = contabilizacionAR;
        compensacion.compensado = compensado;
        compensacion.reporteGenerado = reporteGenerado;
        compensacion.montoVentas = montoVentas;
        compensacion.montoAprobadas = montoAprobadas;
        compensacion.perdidas = perdidas;
        compensacion.margenNeto = margenNeto;
        compensacion.margenPct = margenPct;
        compensacion.saldoConfirmado = saldoConfirmado;
        compensacion.cerrado = cerrado;
        compensacion.estado = estado;
        compensacion.fecha = fecha;
        compensacion.fechaCierre = fechaCierre;
        return compensacion;
    }
}

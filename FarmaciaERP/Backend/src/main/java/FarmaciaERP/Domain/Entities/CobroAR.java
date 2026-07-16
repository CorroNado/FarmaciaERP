package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Enums.EstadoCobroAR;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * FI-AR · Fase 05 — Procesamiento de Cobros e Imputación Bancaria
 * (RN-AR5-01). El archivo de transferencia bancaria recibido de la
 * entidad recaudadora se interpreta y se concilia contra las
 * comisiones de tarjeta y retenciones esperadas; si no cuadra, se
 * ingresa el ajuste contable por diferencia; una vez cuadrado, el
 * Sistema ERP registra el ingreso de dinero y su imputación en la
 * cuenta del cliente, habilitando la Fase 06.
 */
@Getter
@Setter
public class CobroAR {

    private static final double COMISION_ESPERADA_PCT = 3.5;

    private Long id;
    private ContabilizacionAR contabilizacionAR;
    private double montoTransferido;
    private double retenciones;
    private double comisionPct;
    private double montoConciliado;
    private double diferencia;
    private Boolean cuadra;
    private boolean registrado;
    private EstadoCobroAR estado;
    private LocalDateTime fecha;
    private LocalDateTime fechaRegistro;

    private CobroAR() {
    }

    /**
     * 5.1 - Interpretar Archivo de Transferencia bancaria recibido de la
     * entidad recaudadora. RN-AR5-01: el monto transferido resulta del
     * monto esperado del lote (cobertura de la aseguradora, o recetas
     * aprobadas/liberadas más el copago cuando no hay cobertura) menos
     * las retenciones aplicadas por la entidad recaudadora.
     */
    public static CobroAR interpretar(ContabilizacionAR contabilizacionAR, double montoEsperado,
                                       double retenciones) {
        if (contabilizacionAR == null) {
            throw new BadRequestException("El cobro debe originarse del lote de la Fase 02 (Contabilización AR)");
        }
        if (retenciones < 0) {
            throw new BadRequestException("Las retenciones aplicadas no pueden ser negativas");
        }

        CobroAR cobro = new CobroAR();
        cobro.contabilizacionAR = contabilizacionAR;
        cobro.retenciones = retenciones;
        cobro.montoTransferido = Math.round((montoEsperado - retenciones) * 100) / 100.0;
        cobro.estado = EstadoCobroAR.INTERPRETADO;
        cobro.fecha = LocalDateTime.now();
        return cobro;
    }

    /**
     * 5.2 - Conciliar Comisiones de Tarjetas y Retenciones. RN-AR5-01: se
     * calcula el monto neto conciliado a partir del porcentaje de
     * comisión estimado y se compara contra el neto esperado (comisión
     * estándar de tarjeta del 3.5%); si la diferencia es despreciable
     * (menor a 0.01), los montos cuadran.
     */
    public void conciliarComisionesRetenciones(double comisionPct) {
        if (this.estado != EstadoCobroAR.INTERPRETADO) {
            throw new BadRequestException(
                    "RN-AR5-01: el archivo de transferencia debe interpretarse antes de conciliar comisiones y retenciones");
        }
        if (comisionPct < 0) {
            throw new BadRequestException("El porcentaje de comisión de tarjeta no puede ser negativo");
        }

        this.comisionPct = comisionPct;
        double comision = this.montoTransferido * (comisionPct / 100);
        this.montoConciliado = Math.round((this.montoTransferido - comision) * 100) / 100.0;

        double esperadoNeto = Math.round((this.montoTransferido - (this.montoTransferido * (COMISION_ESPERADA_PCT / 100))) * 100) / 100.0;
        this.diferencia = Math.round((this.montoConciliado - esperadoNeto) * 100) / 100.0;
        this.cuadra = Math.abs(this.diferencia) < 0.01;
        this.estado = this.cuadra ? EstadoCobroAR.CONCILIADO : EstadoCobroAR.DESCUADRADO;
    }

    /**
     * 5.3 - Ingresar Ajuste Contable por Diferencia. RN-AR5-01: solo
     * aplica cuando la conciliación de comisiones y retenciones detectó
     * una diferencia; el ajuste absorbe la diferencia y deja los montos
     * cuadrados para continuar el ciclo.
     */
    public void ingresarAjusteContablePorDiferencia() {
        if (this.estado != EstadoCobroAR.DESCUADRADO) {
            throw new BadRequestException(
                    "RN-AR5-01: el ajuste contable por diferencia solo aplica cuando la conciliación detectó un descuadre");
        }

        this.montoConciliado = Math.round((this.montoConciliado - this.diferencia) * 100) / 100.0;
        this.cuadra = true;
        this.estado = EstadoCobroAR.AJUSTADO;
    }

    /**
     * 6.1 - Registrar Ingreso de Dinero e Imputación en la cuenta del
     * cliente (Sistema ERP). RN-AR5-01: solo puede registrarse una vez
     * que los montos y retenciones quedaron cuadrados (conciliados
     * directamente o mediante el ajuste contable por diferencia).
     */
    public void registrarIngresoImputacion() {
        if (!Boolean.TRUE.equals(this.cuadra)) {
            throw new BadRequestException(
                    "RN-AR5-01: los montos y retenciones deben estar cuadrados antes de registrar el ingreso e imputación");
        }
        if (this.registrado) {
            throw new BadRequestException("El ingreso de dinero ya fue registrado e imputado");
        }

        this.registrado = true;
        this.estado = EstadoCobroAR.REGISTRADO;
        this.fechaRegistro = LocalDateTime.now();
    }

    /**
     * RN-AR5-01: condición de salida de la Fase 05 hacia la Fase 06 —
     * Compensación Final y Análisis de Margen Neto.
     */
    public boolean puedeContinuarFase06() {
        return this.estado == EstadoCobroAR.REGISTRADO;
    }

    /**
     * Reconstrucción desde persistencia (usada por el mapper).
     */
    public static CobroAR reconstruir(Long id, ContabilizacionAR contabilizacionAR, double montoTransferido,
                                       double retenciones, double comisionPct, double montoConciliado,
                                       double diferencia, Boolean cuadra, boolean registrado,
                                       EstadoCobroAR estado, LocalDateTime fecha, LocalDateTime fechaRegistro) {
        CobroAR cobro = new CobroAR();
        cobro.id = id;
        cobro.contabilizacionAR = contabilizacionAR;
        cobro.montoTransferido = montoTransferido;
        cobro.retenciones = retenciones;
        cobro.comisionPct = comisionPct;
        cobro.montoConciliado = montoConciliado;
        cobro.diferencia = diferencia;
        cobro.cuadra = cuadra;
        cobro.registrado = registrado;
        cobro.estado = estado;
        cobro.fecha = fecha;
        cobro.fechaRegistro = fechaRegistro;
        return cobro;
    }
}

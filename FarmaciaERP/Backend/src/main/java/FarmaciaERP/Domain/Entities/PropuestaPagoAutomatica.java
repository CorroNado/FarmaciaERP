package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Enums.EstadoPropuestaPago;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * FI-AP.05 Procesamiento Automático y Propuesta de Pago - Fase 05: a partir
 * del lote de pagos ya conciliado a nivel de gestión (Fase 04), el
 * Analista de Cuentas por Pagar introduce los parámetros de pago en F110 y
 * el sistema ERP ejecuta la propuesta de pago automática; el analista
 * revisa el reporte de excepciones y bloqueos antes de aprobar la
 * propuesta final, ejecutar el pago y generar los archivos bancarios
 * planos (IDoc / N43) que habilitan la Fase 06 (Dispersión Bancaria y
 * Conciliación de Cierre).
 */
@Getter
@Setter
public class PropuestaPagoAutomatica {

    private Long id;
    private String numero;
    private LotePagoTesoreria lotePagoTesoreria;
    private String sociedad;
    private String viaPago;
    private String fechaPago;
    private boolean parametrosIntroducidos;
    private boolean propuestaEjecutada;
    private double montoPropuesta;
    private int intentos;
    private Boolean propuestaCorrecta;
    private boolean propuestaAprobada;
    private boolean pagoEjecutado;
    private boolean archivosGenerados;
    private EstadoPropuestaPago estado;
    private LocalDateTime fecha;

    private PropuestaPagoAutomatica() {
    }

    /**
     * RN-AP5-01: la propuesta de pago automática sólo puede iniciarse a
     * partir de un lote de pagos ya ejecutado y conciliado a nivel de
     * gestión (Fase 04 concluida).
     */
    public static PropuestaPagoAutomatica iniciar(String numero, LotePagoTesoreria lotePagoTesoreria) {
        if (numero == null || numero.isBlank()) {
            throw new BadRequestException("El número de la propuesta de pago es obligatorio");
        }
        if (lotePagoTesoreria == null) {
            throw new BadRequestException("La propuesta de pago debe asociarse a un lote de pagos de Tesorería");
        }
        if (!lotePagoTesoreria.estaConciliado()) {
            throw new BadRequestException(
                    "RN-AP5-01: el lote " + lotePagoTesoreria.getNumero()
                            + " debe estar ejecutado y conciliado a nivel de gestión (Fase 04 concluida) "
                            + "antes de iniciar la propuesta de pago automática");
        }

        PropuestaPagoAutomatica propuesta = new PropuestaPagoAutomatica();
        propuesta.numero = numero;
        propuesta.lotePagoTesoreria = lotePagoTesoreria;
        propuesta.montoPropuesta = lotePagoTesoreria.getMontoLote();
        propuesta.parametrosIntroducidos = false;
        propuesta.propuestaEjecutada = false;
        propuesta.intentos = 0;
        propuesta.propuestaCorrecta = null;
        propuesta.propuestaAprobada = false;
        propuesta.pagoEjecutado = false;
        propuesta.archivosGenerados = false;
        propuesta.estado = EstadoPropuestaPago.EN_PARAMETRIZACION;
        propuesta.fecha = LocalDateTime.now();
        return propuesta;
    }

    /**
     * Paso 5.1 - Introducir Parámetros de Pago en F110 (fecha, sociedad,
     * vía de pago, lote de proveedores) — Analista de Cuentas por Pagar.
     */
    public void introducirParametrosPago(String sociedad, String viaPago, String fechaPago) {
        if (parametrosIntroducidos) {
            throw new BadRequestException(
                    "RN-AP5-02: los parámetros de pago de " + numero + " ya fueron introducidos");
        }
        if (sociedad == null || sociedad.isBlank()) {
            throw new BadRequestException("RN-AP5-02: la sociedad es obligatoria para parametrizar el pago");
        }
        if (viaPago == null || viaPago.isBlank()) {
            throw new BadRequestException("RN-AP5-02: la vía de pago es obligatoria para parametrizar el pago");
        }
        if (fechaPago == null || fechaPago.isBlank()) {
            throw new BadRequestException("RN-AP5-02: la fecha de pago es obligatoria para parametrizar el pago");
        }
        this.sociedad = sociedad;
        this.viaPago = viaPago;
        this.fechaPago = fechaPago;
        this.parametrosIntroducidos = true;
    }

    /**
     * Paso 5.1 (cont.) - Ejecutar Propuesta de Pago Automática (Sistema
     * ERP) sobre el lote validado por Tesorería.
     */
    public void ejecutarPropuestaAutomatica() {
        if (!parametrosIntroducidos) {
            throw new BadRequestException(
                    "RN-AP5-03: deben introducirse los parámetros de pago en F110 antes de ejecutar la propuesta");
        }
        if (propuestaEjecutada) {
            throw new BadRequestException(
                    "RN-AP5-03: la propuesta de pago de " + numero + " ya fue ejecutada; revise el reporte de excepciones");
        }
        this.intentos++;
        this.propuestaEjecutada = true;
        this.propuestaCorrecta = null;
        this.estado = EstadoPropuestaPago.PROPUESTA_EJECUTADA;
    }

    /**
     * Paso 5.2 - Revisar Reporte de Excepciones y Bloqueos (Analista de
     * Cuentas por Pagar). RN-AP5-04: el primer intento queda observado por
     * datos bancarios desactualizados de un proveedor; a partir del
     * segundo intento (ya reejecutado tras el ajuste) la propuesta resulta
     * correcta.
     */
    public void revisarReporteExcepciones() {
        if (!propuestaEjecutada) {
            throw new BadRequestException(
                    "RN-AP5-04: la propuesta de pago debe estar ejecutada antes de revisar el reporte de excepciones");
        }
        if (propuestaCorrecta != null) {
            throw new BadRequestException(
                    "RN-AP5-04: el reporte de excepciones de " + numero + " ya fue revisado en este intento");
        }
        this.propuestaCorrecta = intentos > 1;
        this.estado = propuestaCorrecta ? EstadoPropuestaPago.PROPUESTA_EJECUTADA : EstadoPropuestaPago.CON_EXCEPCIONES;
    }

    /**
     * Paso 5.2 (cont.) - Ajustar Parámetros y Reejecutar Propuesta
     * (Sistema ERP), cuando la revisión detectó excepciones o bloqueos.
     */
    public void ajustarParametrosYReejecutar() {
        if (propuestaCorrecta == null || propuestaCorrecta) {
            throw new BadRequestException(
                    "RN-AP5-05: sólo corresponde ajustar los parámetros cuando el reporte de excepciones detectó bloqueos");
        }
        this.propuestaEjecutada = false;
        this.propuestaCorrecta = null;
    }

    /**
     * Paso 5.2 (cont.) - Aprobar Propuesta de Pago Final (Analista de
     * Cuentas por Pagar), sin excepciones ni bloqueos pendientes.
     */
    public void aprobarPropuestaFinal() {
        if (propuestaCorrecta == null || !propuestaCorrecta) {
            throw new BadRequestException(
                    "RN-AP5-06: la propuesta de pago debe estar revisada y sin excepciones antes de aprobarse");
        }
        if (propuestaAprobada) {
            throw new BadRequestException("RN-AP5-06: la propuesta de pago " + numero + " ya fue aprobada");
        }
        this.propuestaAprobada = true;
        this.estado = EstadoPropuestaPago.APROBADA;
    }

    /**
     * Paso 5.3 - Ejecutar Ejecución de Pago (Sistema ERP) sobre la
     * propuesta final aprobada.
     */
    public void ejecutarPago() {
        if (!propuestaAprobada) {
            throw new BadRequestException(
                    "RN-AP5-07: la propuesta de pago final debe estar aprobada antes de ejecutar el pago");
        }
        if (pagoEjecutado) {
            throw new BadRequestException("RN-AP5-07: el pago de la propuesta " + numero + " ya fue ejecutado");
        }
        this.pagoEjecutado = true;
        this.estado = EstadoPropuestaPago.PAGO_EJECUTADO;
    }

    /**
     * Paso 5.3 (cont.) - Generar Archivos Bancarios Planos (IDoc / N43)
     * para envío multi-bancario. Concluye la Fase 05 y habilita la
     * Fase 06 (Dispersión Bancaria y Conciliación de Cierre).
     */
    public void generarArchivosBancarios() {
        if (!pagoEjecutado) {
            throw new BadRequestException(
                    "RN-AP5-08: el pago debe estar ejecutado en SAP antes de generar los archivos bancarios planos");
        }
        if (archivosGenerados) {
            throw new BadRequestException(
                    "RN-AP5-08: los archivos bancarios de la propuesta " + numero + " ya fueron generados");
        }
        this.archivosGenerados = true;
        this.estado = EstadoPropuestaPago.CONCLUIDA;
    }

    public boolean estaConcluida() {
        return estado == EstadoPropuestaPago.CONCLUIDA;
    }

    /**
     * Reconstrucción desde persistencia (usada por el mapper).
     */
    public static PropuestaPagoAutomatica reconstruir(Long id, String numero, LotePagoTesoreria lotePagoTesoreria,
                                                        String sociedad, String viaPago, String fechaPago,
                                                        boolean parametrosIntroducidos, boolean propuestaEjecutada,
                                                        double montoPropuesta, int intentos, Boolean propuestaCorrecta,
                                                        boolean propuestaAprobada, boolean pagoEjecutado,
                                                        boolean archivosGenerados, EstadoPropuestaPago estado,
                                                        LocalDateTime fecha) {
        PropuestaPagoAutomatica propuesta = new PropuestaPagoAutomatica();
        propuesta.id = id;
        propuesta.numero = numero;
        propuesta.lotePagoTesoreria = lotePagoTesoreria;
        propuesta.sociedad = sociedad;
        propuesta.viaPago = viaPago;
        propuesta.fechaPago = fechaPago;
        propuesta.parametrosIntroducidos = parametrosIntroducidos;
        propuesta.propuestaEjecutada = propuestaEjecutada;
        propuesta.montoPropuesta = montoPropuesta;
        propuesta.intentos = intentos;
        propuesta.propuestaCorrecta = propuestaCorrecta;
        propuesta.propuestaAprobada = propuestaAprobada;
        propuesta.pagoEjecutado = pagoEjecutado;
        propuesta.archivosGenerados = archivosGenerados;
        propuesta.estado = estado;
        propuesta.fecha = fecha;
        return propuesta;
    }
}

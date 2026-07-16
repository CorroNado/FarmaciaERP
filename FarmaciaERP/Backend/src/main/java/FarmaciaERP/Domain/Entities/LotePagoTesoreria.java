package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Enums.EstadoLotePago;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * FI-AP.04 Estrategia de Tesorería y Riesgo Sanitario - Fase 04: el
 * Analista de Cuentas por Pagar prioriza a los proveedores críticos de
 * medicamentos, el Gerente de Tesorería negocia el descuento por pronto
 * pago y se prepara el lote de pagos (F110 SAP), sujeto a verificación de
 * fondos y a la aprobación del Comité Semanal de Tesorería antes de
 * ejecutar y conciliar los pagos a nivel de gestión, habilitando la
 * Fase 05 (Procesamiento Automático y Propuesta de Pago).
 */
@Getter
@Setter
public class LotePagoTesoreria {

    private Long id;
    private String numero;
    private List<AjusteContableRegularizacion> ajustesContables;
    private double montoNetoRegularizado;
    private boolean proveedoresCriticosPriorizados;
    private boolean descuentoProntoPagoNegociado;
    private double descuentoProntoPagoPct;
    private boolean lotePreparado;
    private double montoLote;
    private boolean fondosVerificados;
    private int revisionesComite;
    private boolean loteCorregido;
    private boolean aprobadoPorComite;
    private boolean pagosConciliadosGestion;
    private EstadoLotePago estado;
    private LocalDateTime fecha;

    private LotePagoTesoreria() {
    }

    /**
     * RN-AP4-01: el lote de pagos sólo puede armarse a partir de ajustes
     * contables ya regularizados (Fase 03 concluida) para cada factura
     * incluida.
     */
    public static LotePagoTesoreria iniciar(String numero, List<AjusteContableRegularizacion> ajustesContables) {
        if (numero == null || numero.isBlank()) {
            throw new BadRequestException("El número del lote de pagos es obligatorio");
        }
        if (ajustesContables == null || ajustesContables.isEmpty()) {
            throw new BadRequestException(
                    "RN-AP4-01: el lote de pagos debe incluir al menos un ajuste contable regularizado");
        }
        for (AjusteContableRegularizacion ajuste : ajustesContables) {
            if (!ajuste.estaRegularizada()) {
                throw new BadRequestException(
                        "RN-AP4-01: el ajuste " + ajuste.getNumero()
                                + " debe estar regularizado (Fase 03 concluida) antes de incluirse en el lote de pagos");
            }
        }

        LotePagoTesoreria lote = new LotePagoTesoreria();
        lote.numero = numero;
        lote.ajustesContables = ajustesContables;
        lote.montoNetoRegularizado = ajustesContables.stream()
                .mapToDouble(AjusteContableRegularizacion::getMontoNetoPagar).sum();
        lote.proveedoresCriticosPriorizados = false;
        lote.descuentoProntoPagoNegociado = false;
        lote.descuentoProntoPagoPct = 0;
        lote.lotePreparado = false;
        lote.montoLote = 0;
        lote.fondosVerificados = false;
        lote.revisionesComite = 0;
        lote.loteCorregido = false;
        lote.aprobadoPorComite = false;
        lote.pagosConciliadosGestion = false;
        lote.estado = EstadoLotePago.EN_PREPARACION;
        lote.fecha = LocalDateTime.now();
        return lote;
    }

    /**
     * Paso 4.1 - Priorizar Proveedores Críticos de Medicamentos (Analista
     * de Cuentas por Pagar).
     */
    public void priorizarProveedoresCriticos() {
        if (proveedoresCriticosPriorizados) {
            throw new BadRequestException(
                    "RN-AP4-02: los proveedores críticos de " + numero + " ya fueron priorizados");
        }
        this.proveedoresCriticosPriorizados = true;
    }

    /**
     * Paso 4.2 - Negociar Descuento por Pronto Pago (Gerente de Tesorería).
     */
    public void negociarDescuentoProntoPago(double descuentoPct) {
        if (!proveedoresCriticosPriorizados) {
            throw new BadRequestException(
                    "RN-AP4-03: deben priorizarse los proveedores críticos antes de negociar el descuento por pronto pago");
        }
        if (descuentoProntoPagoNegociado) {
            throw new BadRequestException("RN-AP4-03: el descuento por pronto pago de " + numero + " ya fue negociado");
        }
        if (descuentoPct < 0 || descuentoPct > 100) {
            throw new BadRequestException("RN-AP4-03: el descuento por pronto pago debe estar entre 0% y 100%");
        }
        this.descuentoProntoPagoPct = descuentoPct;
        this.descuentoProntoPagoNegociado = true;
    }

    /**
     * Paso 4.3 - Preparar Lote de Pagos (F110 SAP), aplicando el descuento
     * por pronto pago negociado sobre el monto neto regularizado.
     */
    public void prepararLotePagos() {
        if (!descuentoProntoPagoNegociado) {
            throw new BadRequestException(
                    "RN-AP4-04: debe negociarse el descuento por pronto pago antes de preparar el lote de pagos");
        }
        if (lotePreparado) {
            throw new BadRequestException("RN-AP4-04: el lote de pagos " + numero + " ya fue preparado");
        }
        this.montoLote = Math.round(montoNetoRegularizado * (1 - descuentoProntoPagoPct / 100) * 100) / 100.0;
        this.lotePreparado = true;
    }

    /**
     * Paso 4.4 - Verificar Fondos y Validar Lote (Gerente de Tesorería).
     */
    public void verificarFondosYValidarLote() {
        if (!lotePreparado) {
            throw new BadRequestException(
                    "RN-AP4-05: el lote de pagos debe estar preparado antes de verificar fondos");
        }
        if (fondosVerificados) {
            throw new BadRequestException("RN-AP4-05: los fondos de " + numero + " ya fueron verificados");
        }
        this.fondosVerificados = true;
        this.estado = EstadoLotePago.PENDIENTE_APROBACION;
    }

    /**
     * Paso 4.4 (cont.) - ¿Lote Aprobado? Se somete el lote al Comité
     * Semanal de Tesorería. La primera revisión queda con observaciones;
     * las siguientes (tras corregir) aprueban el lote.
     */
    public void someterAComite() {
        if (!fondosVerificados) {
            throw new BadRequestException(
                    "RN-AP4-06: deben verificarse los fondos antes de someter el lote al Comité de Tesorería");
        }
        if (aprobadoPorComite) {
            throw new BadRequestException("RN-AP4-06: el lote " + numero + " ya fue aprobado por el comité");
        }
        if (revisionesComite >= 1 && !loteCorregido) {
            throw new BadRequestException(
                    "RN-AP4-06: debe corregirse el lote según las observaciones del comité antes de volver a someterlo");
        }
        this.revisionesComite++;
        if (revisionesComite == 1) {
            this.estado = EstadoLotePago.CON_OBSERVACIONES;
        } else {
            this.aprobadoPorComite = true;
            this.estado = EstadoLotePago.APROBADO;
        }
    }

    /**
     * Paso 4.4 (cont.) - Corregir Lote según Observaciones del comité.
     */
    public void corregirLoteSegunObservaciones() {
        if (estado != EstadoLotePago.CON_OBSERVACIONES) {
            throw new BadRequestException(
                    "RN-AP4-07: sólo corresponde corregir el lote " + numero + " cuando el comité registró observaciones");
        }
        if (loteCorregido) {
            throw new BadRequestException("RN-AP4-07: el lote " + numero + " ya fue corregido");
        }
        this.loteCorregido = true;
    }

    /**
     * Paso 4.5 - Ejecutar Pagos y Conciliar en SAP FI-AP (Analista de
     * Cuentas por Pagar). RN-AP4-08: concluye la Fase 04 a nivel de
     * gestión y habilita la Fase 05 (Procesamiento Automático de Pago).
     */
    public void ejecutarPagosYConciliar() {
        if (!aprobadoPorComite) {
            throw new BadRequestException(
                    "RN-AP4-08: el lote debe estar aprobado por el Comité de Tesorería antes de ejecutar y conciliar los pagos");
        }
        if (pagosConciliadosGestion) {
            throw new BadRequestException("RN-AP4-08: los pagos del lote " + numero + " ya fueron conciliados");
        }
        this.pagosConciliadosGestion = true;
        this.estado = EstadoLotePago.CONCILIADO_GESTION;
    }

    public boolean estaConciliado() {
        return estado == EstadoLotePago.CONCILIADO_GESTION;
    }

    /**
     * Reconstrucción desde persistencia (usada por el mapper).
     */
    public static LotePagoTesoreria reconstruir(Long id, String numero, List<AjusteContableRegularizacion> ajustesContables,
                                                 double montoNetoRegularizado, boolean proveedoresCriticosPriorizados,
                                                 boolean descuentoProntoPagoNegociado, double descuentoProntoPagoPct,
                                                 boolean lotePreparado, double montoLote, boolean fondosVerificados,
                                                 int revisionesComite, boolean loteCorregido, boolean aprobadoPorComite,
                                                 boolean pagosConciliadosGestion, EstadoLotePago estado, LocalDateTime fecha) {
        LotePagoTesoreria lote = new LotePagoTesoreria();
        lote.id = id;
        lote.numero = numero;
        lote.ajustesContables = ajustesContables;
        lote.montoNetoRegularizado = montoNetoRegularizado;
        lote.proveedoresCriticosPriorizados = proveedoresCriticosPriorizados;
        lote.descuentoProntoPagoNegociado = descuentoProntoPagoNegociado;
        lote.descuentoProntoPagoPct = descuentoProntoPagoPct;
        lote.lotePreparado = lotePreparado;
        lote.montoLote = montoLote;
        lote.fondosVerificados = fondosVerificados;
        lote.revisionesComite = revisionesComite;
        lote.loteCorregido = loteCorregido;
        lote.aprobadoPorComite = aprobadoPorComite;
        lote.pagosConciliadosGestion = pagosConciliadosGestion;
        lote.estado = estado;
        lote.fecha = fecha;
        return lote;
    }
}

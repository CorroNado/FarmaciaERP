package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Enums.EstadoCierreCaja;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * FI-AR · Fase 01 — Recepción y Auditoría del Cierre de Venta (POS-SD).
 * RN-AR1-01: el cierre de caja diario (reporte consolidado de ventas del
 * mostrador) se concilia contra el arqueo físico de la sucursal. Toda
 * variación debe justificarse y remitirse a auditoría médica antes de
 * habilitar la clasificación automática de copagos y coberturas de
 * aseguradoras que libera el ciclo a la Fase 02 (Contabilización).
 */
@Getter
@Setter
public class CierreCaja {

    private Long id;
    private String numero;
    private Sucursal sucursal;
    private LocalDateTime fecha;
    private double reporteVentas;
    private Double arqueo;
    private double diferencia;
    private Boolean cuadra;
    private String justificacion;
    private boolean fisicosEnviados;
    private double copago;
    private double coberturaAseg;
    private EstadoCierreCaja estado;

    private CierreCaja() {
    }

    /**
     * 1.1 - Emitir Reporte Consolidado de Ventas del Mostrador: el sistema
     * ERP totaliza el cierre de caja ejecutado en la sucursal.
     */
    public static CierreCaja abrir(String numero, Sucursal sucursal, double reporteVentas) {
        if (numero == null || numero.isBlank()) {
            throw new BadRequestException("El número de cierre de caja es obligatorio");
        }
        if (sucursal == null) {
            throw new BadRequestException("El cierre de caja debe estar asociado a una sucursal");
        }
        if (reporteVentas < 0) {
            throw new BadRequestException("El reporte consolidado de ventas no puede ser negativo");
        }

        CierreCaja cierre = new CierreCaja();
        cierre.numero = numero;
        cierre.sucursal = sucursal;
        cierre.reporteVentas = reporteVentas;
        cierre.fecha = LocalDateTime.now();
        cierre.estado = EstadoCierreCaja.ABIERTO;
        return cierre;
    }

    /**
     * 1.2 - Realizar Arqueo Físico vs. Valores Registrados en Pantalla.
     * RN-AR1-01: se considera cuadrado si la diferencia absoluta es menor a
     * S/ 0.01.
     */
    public void registrarArqueo(double montoContado) {
        if (this.arqueo != null) {
            throw new BadRequestException("El arqueo físico de este cierre ya fue registrado");
        }
        this.arqueo = montoContado;
        this.diferencia = montoContado - this.reporteVentas;
        this.cuadra = Math.abs(this.diferencia) < 0.01;
        this.estado = this.cuadra ? EstadoCierreCaja.CUADRADO : EstadoCierreCaja.CON_VARIACION;
    }

    /**
     * 2.1 - Registrar Justificación de Faltante o Sobrante en Sistema.
     * Solo aplica cuando el arqueo detectó una variación.
     */
    public void registrarJustificacion(String justificacion) {
        if (this.arqueo == null || Boolean.TRUE.equals(this.cuadra)) {
            throw new BadRequestException("Solo se justifican los cierres de caja con variación en el arqueo");
        }
        if (justificacion == null || justificacion.isBlank()) {
            throw new BadRequestException("Debe registrar una justificación del descuadre");
        }
        if (this.justificacion != null) {
            throw new BadRequestException("Este cierre de caja ya tiene una justificación registrada");
        }
        this.justificacion = justificacion;
        this.estado = EstadoCierreCaja.JUSTIFICADO;
    }

    /**
     * Fase 1 (rama de variación) - Enviar Físicos de Recetas Médicas a
     * Oficina Central para su verificación posterior por el Auditor Médico
     * Corporativo.
     */
    public void enviarFisicosRecetas() {
        if (this.justificacion == null) {
            throw new BadRequestException("Debe registrar la justificación del descuadre antes de enviar los físicos de recetas");
        }
        if (this.fisicosEnviados) {
            throw new BadRequestException("Los físicos de recetas ya fueron enviados a Oficina Central");
        }
        this.fisicosEnviados = true;
        this.estado = EstadoCierreCaja.LIBERADO;
    }

    /**
     * 1.3 - Clasificar de forma Automática Copagos y Coberturas de
     * Aseguradoras. RN-AR1-01: solo puede ejecutarse si la caja cuadró al
     * 100% o, en caso de variación, si ya se enviaron los físicos de las
     * recetas a Oficina Central.
     */
    public void clasificarCopagoCobertura() {
        if (this.arqueo == null) {
            throw new BadRequestException("Debe registrarse el arqueo físico antes de clasificar copagos y coberturas");
        }
        boolean habilitado = Boolean.TRUE.equals(this.cuadra) || this.fisicosEnviados;
        if (!habilitado) {
            throw new BadRequestException(
                    "El cierre de caja tiene una variación sin justificar o sin envío de físicos de recetas a Oficina Central");
        }
        if (this.copago > 0) {
            throw new BadRequestException("Los copagos y coberturas de este cierre ya fueron clasificados");
        }
        this.copago = this.reporteVentas * 0.30;
        this.coberturaAseg = this.reporteVentas * 0.70;
        this.estado = EstadoCierreCaja.CLASIFICADO;
    }

    /**
     * RN-AR1-01: condición de salida de la Fase 01 hacia la Fase 02 —
     * Contabilización y Declaración de Valores.
     */
    public boolean puedeContinuarFase02() {
        return this.estado == EstadoCierreCaja.CLASIFICADO;
    }

    /**
     * Reconstrucción desde persistencia (usada por el mapper).
     */
    public static CierreCaja reconstruir(Long id, String numero, Sucursal sucursal, LocalDateTime fecha,
                                          double reporteVentas, Double arqueo, double diferencia, Boolean cuadra,
                                          String justificacion, boolean fisicosEnviados, double copago,
                                          double coberturaAseg, EstadoCierreCaja estado) {
        CierreCaja cierre = new CierreCaja();
        cierre.id = id;
        cierre.numero = numero;
        cierre.sucursal = sucursal;
        cierre.fecha = fecha;
        cierre.reporteVentas = reporteVentas;
        cierre.arqueo = arqueo;
        cierre.diferencia = diferencia;
        cierre.cuadra = cuadra;
        cierre.justificacion = justificacion;
        cierre.fisicosEnviados = fisicosEnviados;
        cierre.copago = copago;
        cierre.coberturaAseg = coberturaAseg;
        cierre.estado = estado;
        return cierre;
    }
}

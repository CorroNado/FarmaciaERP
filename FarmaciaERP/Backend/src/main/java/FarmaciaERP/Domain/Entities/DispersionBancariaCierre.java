package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Enums.EstadoDispersionCierre;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * FI-AP.06 Dispersión Bancaria y Conciliación de Cierre - Fase 06: el
 * sistema ERP compila la propuesta de pago (F110) recibida de la Fase 05;
 * el Comprador / Category Manager valida el lote contra duplicados y
 * bloqueos antes de que el sistema genere el archivo bancario plano
 * (IDoc); el Analista de Cuentas por Pagar aplica la firma digital con
 * token bancario y ejecuta las transferencias en banca empresa; y el
 * Gerente de Finanzas / Tesorero importa el extracto bancario digital del
 * día (FF.5) y concilia las cuentas puente financieras, extinguiendo la
 * obligación con el proveedor y cerrando el ciclo FI-AP.
 */
@Getter
@Setter
public class DispersionBancariaCierre {

    private Long id;
    private String numero;
    private PropuestaPagoAutomatica propuestaPagoAutomatica;
    private double montoDispersion;
    private boolean propuestaCompilada;
    private Boolean propuestaValidada;
    private int intentosValidacion;
    private boolean loteCorregido;
    private boolean archivoGenerado;
    private boolean firmado;
    private boolean transferenciasEjecutadas;
    private boolean extractoImportado;
    private boolean conciliado;
    private boolean obligacionExtinguida;
    private EstadoDispersionCierre estado;
    private LocalDateTime fecha;

    private DispersionBancariaCierre() {
    }

    /**
     * RN-AP6-01: la dispersión bancaria de cierre sólo puede iniciarse a
     * partir de una propuesta de pago automática ya concluida (Fase 05
     * finalizada: pago ejecutado y archivos bancarios generados).
     */
    public static DispersionBancariaCierre iniciar(String numero, PropuestaPagoAutomatica propuestaPagoAutomatica) {
        if (numero == null || numero.isBlank()) {
            throw new BadRequestException("El número de la dispersión bancaria de cierre es obligatorio");
        }
        if (propuestaPagoAutomatica == null) {
            throw new BadRequestException(
                    "La dispersión bancaria de cierre debe asociarse a una propuesta de pago automática");
        }
        if (!propuestaPagoAutomatica.estaConcluida()) {
            throw new BadRequestException(
                    "RN-AP6-01: la propuesta de pago " + propuestaPagoAutomatica.getNumero()
                            + " debe estar concluida (Fase 05 finalizada) antes de iniciar la dispersión bancaria de cierre");
        }

        DispersionBancariaCierre dispersion = new DispersionBancariaCierre();
        dispersion.numero = numero;
        dispersion.propuestaPagoAutomatica = propuestaPagoAutomatica;
        dispersion.montoDispersion = propuestaPagoAutomatica.getMontoPropuesta();
        dispersion.propuestaCompilada = false;
        dispersion.propuestaValidada = null;
        dispersion.intentosValidacion = 0;
        dispersion.loteCorregido = false;
        dispersion.archivoGenerado = false;
        dispersion.firmado = false;
        dispersion.transferenciasEjecutadas = false;
        dispersion.extractoImportado = false;
        dispersion.conciliado = false;
        dispersion.obligacionExtinguida = false;
        dispersion.estado = EstadoDispersionCierre.EN_COMPILACION;
        dispersion.fecha = LocalDateTime.now();
        return dispersion;
    }

    /**
     * Paso 6.1 - Compilar Propuesta de Pago (F110) recibida desde la
     * Fase 05 (Sistema ERP).
     */
    public void compilarPropuestaPago() {
        if (propuestaCompilada) {
            throw new BadRequestException(
                    "RN-AP6-02: la propuesta de pago de " + numero + " ya fue compilada");
        }
        this.propuestaCompilada = true;
        this.estado = EstadoDispersionCierre.PENDIENTE_VALIDACION;
    }

    /**
     * Paso 6.2 - Validar Propuesta de Duplicados / Bloqueos (Comprador /
     * Category Manager). RN-AP6-03: el primer intento detecta una
     * posible transferencia duplicada; a partir del segundo intento
     * (o si el lote ya fue corregido) la propuesta resulta validada.
     */
    public void validarPropuestaDuplicadosBloqueos() {
        if (!propuestaCompilada) {
            throw new BadRequestException(
                    "RN-AP6-03: la propuesta de pago debe estar compilada antes de validar duplicados y bloqueos");
        }
        if (Boolean.TRUE.equals(propuestaValidada)) {
            throw new BadRequestException("RN-AP6-03: el lote de " + numero + " ya fue validado");
        }
        this.intentosValidacion++;
        this.propuestaValidada = intentosValidacion > 1 || loteCorregido;
        this.estado = propuestaValidada ? EstadoDispersionCierre.VALIDADA : EstadoDispersionCierre.CON_OBSERVACIONES;
    }

    /**
     * Paso 6.2 (cont.) - Corregir Errores y Reenviar Lote (Analista de
     * Cuentas por Pagar), cuando se detectó una posible transferencia
     * duplicada.
     */
    public void corregirErroresYReenviarLote() {
        if (propuestaValidada == null || propuestaValidada) {
            throw new BadRequestException(
                    "RN-AP6-04: sólo corresponde corregir el lote cuando la validación detectó duplicados o bloqueos");
        }
        this.loteCorregido = true;
        this.propuestaValidada = null;
        this.estado = EstadoDispersionCierre.PENDIENTE_VALIDACION;
    }

    /**
     * Paso 6.3 - Generar Archivo Bancario Plano (IDoc) (Sistema ERP), sin
     * duplicados ni bloqueos pendientes.
     */
    public void generarArchivoBancario() {
        if (propuestaValidada == null || !propuestaValidada) {
            throw new BadRequestException(
                    "RN-AP6-05: la propuesta debe estar validada (sin duplicados ni bloqueos) antes de generar el archivo bancario");
        }
        if (archivoGenerado) {
            throw new BadRequestException("RN-AP6-05: el archivo bancario de " + numero + " ya fue generado");
        }
        this.archivoGenerado = true;
        this.estado = EstadoDispersionCierre.ARCHIVO_GENERADO;
    }

    /**
     * Paso 6.4 - Aplicar Firma Digital con Token Bancario (Analista de
     * Cuentas por Pagar).
     */
    public void aplicarFirmaDigital() {
        if (!archivoGenerado) {
            throw new BadRequestException(
                    "RN-AP6-06: el archivo bancario plano (IDoc) debe estar generado antes de aplicar la firma digital");
        }
        if (firmado) {
            throw new BadRequestException("RN-AP6-06: la dispersión " + numero + " ya cuenta con firma digital");
        }
        this.firmado = true;
        this.estado = EstadoDispersionCierre.FIRMADA;
    }

    /**
     * Paso 6.5 - Ejecutar Transferencias en Banca Empresa (Token)
     * (Analista de Cuentas por Pagar).
     */
    public void ejecutarTransferenciasBancarias() {
        if (!firmado) {
            throw new BadRequestException(
                    "RN-AP6-07: el archivo bancario debe estar firmado digitalmente antes de ejecutar las transferencias");
        }
        if (transferenciasEjecutadas) {
            throw new BadRequestException(
                    "RN-AP6-07: las transferencias bancarias de " + numero + " ya fueron ejecutadas");
        }
        this.transferenciasEjecutadas = true;
        this.estado = EstadoDispersionCierre.TRANSFERIDA;
    }

    /**
     * Paso 6.6 - Importar Extracto Bancario Digital del Día (FF.5)
     * (Gerente de Finanzas / Tesorero).
     */
    public void importarExtractoBancario() {
        if (!transferenciasEjecutadas) {
            throw new BadRequestException(
                    "RN-AP6-08: las transferencias bancarias deben estar ejecutadas antes de importar el extracto bancario");
        }
        if (extractoImportado) {
            throw new BadRequestException("RN-AP6-08: el extracto bancario de " + numero + " ya fue importado");
        }
        this.extractoImportado = true;
        this.estado = EstadoDispersionCierre.EXTRACTO_IMPORTADO;
    }

    /**
     * Paso 6.7 - Conciliar Cuentas Puente Financieras y Compensar Cuenta
     * Transitoria del Banco (Gerente de Finanzas / Tesorero). RN-AP6-09:
     * concluye el ciclo FI-AP; se extingue la obligación con el
     * proveedor.
     */
    public void conciliarCuentasPuenteYCompensar() {
        if (!extractoImportado) {
            throw new BadRequestException(
                    "RN-AP6-09: el extracto bancario digital debe estar importado antes de conciliar las cuentas puente");
        }
        if (conciliado) {
            throw new BadRequestException("RN-AP6-09: las cuentas puente de " + numero + " ya fueron conciliadas");
        }
        this.conciliado = true;
        this.obligacionExtinguida = true;
        this.estado = EstadoDispersionCierre.CONCLUIDA;
    }

    public boolean estaConcluida() {
        return estado == EstadoDispersionCierre.CONCLUIDA;
    }

    /**
     * Reconstrucción desde persistencia (usada por el mapper).
     */
    public static DispersionBancariaCierre reconstruir(Long id, String numero,
                                                         PropuestaPagoAutomatica propuestaPagoAutomatica,
                                                         double montoDispersion, boolean propuestaCompilada,
                                                         Boolean propuestaValidada, int intentosValidacion,
                                                         boolean loteCorregido, boolean archivoGenerado,
                                                         boolean firmado, boolean transferenciasEjecutadas,
                                                         boolean extractoImportado, boolean conciliado,
                                                         boolean obligacionExtinguida, EstadoDispersionCierre estado,
                                                         LocalDateTime fecha) {
        DispersionBancariaCierre dispersion = new DispersionBancariaCierre();
        dispersion.id = id;
        dispersion.numero = numero;
        dispersion.propuestaPagoAutomatica = propuestaPagoAutomatica;
        dispersion.montoDispersion = montoDispersion;
        dispersion.propuestaCompilada = propuestaCompilada;
        dispersion.propuestaValidada = propuestaValidada;
        dispersion.intentosValidacion = intentosValidacion;
        dispersion.loteCorregido = loteCorregido;
        dispersion.archivoGenerado = archivoGenerado;
        dispersion.firmado = firmado;
        dispersion.transferenciasEjecutadas = transferenciasEjecutadas;
        dispersion.extractoImportado = extractoImportado;
        dispersion.conciliado = conciliado;
        dispersion.obligacionExtinguida = obligacionExtinguida;
        dispersion.estado = estado;
        dispersion.fecha = fecha;
        return dispersion;
    }
}

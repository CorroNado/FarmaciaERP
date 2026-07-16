package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Enums.EstadoAjusteContable;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * FI-AP.03 Ajustes Contables y Regularización - Fase 03 (Cierre de
 * Transacción): el Área Financiera confirma si se recibió la Nota de
 * Crédito del laboratorio/droguería por la diferencia comercial acordada
 * en la Fase 02. Si no llega, se gestiona el reclamo directamente con el
 * proveedor hasta obtenerla. En ambos casos se ejecuta el asiento contable
 * de regularización y se desbloquea la partida presupuestaria, habilitando
 * la Fase 04 (Estrategia de Tesorería y Riesgo Sanitario).
 */
@Getter
@Setter
public class AjusteContableRegularizacion {

    private Long id;
    private String numero;
    private DisputaComercial disputaComercial;
    private double montoRegularizacion;
    private Boolean recibeNotaCredito;
    private boolean reclamoGestionado;
    private boolean notaCreditoEnviadaProveedor;
    private boolean notaCreditoRegistrada;
    private boolean asientoRegularizacion;
    private boolean desbloqueado;
    private boolean regularizada;
    private EstadoAjusteContable estado;
    private LocalDateTime fecha;

    private AjusteContableRegularizacion() {
    }

    /**
     * RN-AP3-01: el ajuste contable sólo puede iniciarse a partir de una
     * disputa comercial ya resuelta en el workflow del ERP (Fase 02
     * concluida), y no debe existir ya un ajuste iniciado para la misma
     * disputa.
     */
    public static AjusteContableRegularizacion iniciar(String numero, DisputaComercial disputaComercial) {
        if (numero == null || numero.isBlank()) {
            throw new BadRequestException("El número del ajuste contable es obligatorio");
        }
        if (disputaComercial == null) {
            throw new BadRequestException(
                    "RN-AP3-01: el ajuste contable debe originarse de una disputa comercial resuelta");
        }
        if (!disputaComercial.estaResuelta()) {
            throw new BadRequestException(
                    "RN-AP3-01: la disputa " + disputaComercial.getNumero()
                            + " debe estar resuelta en el workflow del ERP antes de iniciar el cierre de transacción");
        }

        AjusteContableRegularizacion ajuste = new AjusteContableRegularizacion();
        ajuste.numero = numero;
        ajuste.disputaComercial = disputaComercial;
        // RN-AP3-02: el monto a regularizar es el impacto financiero acordado
        // en la negociación de la Fase 02.
        ajuste.montoRegularizacion = disputaComercial.getImpactoFinanciero();
        ajuste.recibeNotaCredito = null;
        ajuste.reclamoGestionado = false;
        ajuste.notaCreditoEnviadaProveedor = false;
        ajuste.notaCreditoRegistrada = false;
        ajuste.asientoRegularizacion = false;
        ajuste.desbloqueado = false;
        ajuste.regularizada = false;
        ajuste.estado = EstadoAjusteContable.PENDIENTE_NOTA_CREDITO;
        ajuste.fecha = LocalDateTime.now();
        return ajuste;
    }

    /**
     * Paso 3.1 - ¿Se Recibe Nota de Crédito? El Área Financiera confirma o
     * descarta la recepción de la Nota de Crédito del proveedor.
     */
    public void registrarRecepcionNotaCredito(boolean recibida) {
        if (recibeNotaCredito != null) {
            throw new BadRequestException(
                    "RN-AP3-03: ya se registró la confirmación de recepción de Nota de Crédito para " + numero);
        }
        this.recibeNotaCredito = recibida;
        if (recibida) {
            this.notaCreditoRegistrada = false;
        } else {
            this.estado = EstadoAjusteContable.GESTION_RECLAMO;
        }
    }

    /**
     * Paso 3.1.a - Nota de Crédito NO recibida: el sistema SAP ERP gestiona
     * el reclamo directamente con el laboratorio/droguería.
     */
    public void gestionarReclamo() {
        if (recibeNotaCredito == null || recibeNotaCredito) {
            throw new BadRequestException(
                    "RN-AP3-04: sólo corresponde gestionar el reclamo cuando no se recibió la Nota de Crédito");
        }
        if (reclamoGestionado) {
            throw new BadRequestException("RN-AP3-04: el reclamo de " + numero + " ya fue gestionado");
        }
        this.reclamoGestionado = true;
    }

    /**
     * Paso 3.1.a (cont.) - el laboratorio/droguería evalúa el reclamo y
     * envía la Nota de Crédito, quedando registrada automáticamente en SAP.
     */
    public void evaluarYEnviarNotaCredito() {
        if (!reclamoGestionado) {
            throw new BadRequestException(
                    "RN-AP3-05: debe gestionarse el reclamo con el proveedor antes de recibir su Nota de Crédito");
        }
        if (notaCreditoEnviadaProveedor) {
            throw new BadRequestException("RN-AP3-05: la Nota de Crédito de " + numero + " ya fue enviada");
        }
        this.notaCreditoEnviadaProveedor = true;
        this.notaCreditoRegistrada = true;
        this.estado = EstadoAjusteContable.NOTA_CREDITO_REGISTRADA;
    }

    /**
     * Paso 3.1.b - Nota de Crédito SÍ recibida: se registra directamente en
     * SAP.
     */
    public void registrarNotaCredito() {
        if (recibeNotaCredito == null || !recibeNotaCredito) {
            throw new BadRequestException(
                    "RN-AP3-06: sólo corresponde registrar la Nota de Crédito cuando fue recibida del proveedor");
        }
        if (notaCreditoRegistrada) {
            throw new BadRequestException("RN-AP3-06: la Nota de Crédito de " + numero + " ya fue registrada");
        }
        this.notaCreditoRegistrada = true;
        this.estado = EstadoAjusteContable.NOTA_CREDITO_REGISTRADA;
    }

    /**
     * Paso 3.2 - Ejecutar Asiento de Regularización por diferencias
     * permitidas: (Debe) Gasto por Variación de Precios / (Haber)
     * Proveedores.
     */
    public void ejecutarAsientoRegularizacion() {
        if (!notaCreditoRegistrada) {
            throw new BadRequestException(
                    "RN-AP3-07: la Nota de Crédito debe estar registrada en SAP antes de ejecutar el asiento de regularización");
        }
        if (asientoRegularizacion) {
            throw new BadRequestException("RN-AP3-07: el asiento de regularización de " + numero + " ya fue ejecutado");
        }
        this.asientoRegularizacion = true;
    }

    /**
     * Paso 3.3 - Desbloquear Partida Presupuestaria / Factura en MRBR y
     * actualizar el estado de pago. RN-AP3-08: concluye la Fase 03 y
     * habilita la Fase 04 (Estrategia de Tesorería y Riesgo Sanitario).
     */
    public void desbloquearPartida() {
        if (!asientoRegularizacion) {
            throw new BadRequestException(
                    "RN-AP3-08: debe ejecutarse el asiento de regularización antes de desbloquear la partida presupuestaria");
        }
        if (desbloqueado) {
            throw new BadRequestException("RN-AP3-08: la partida de " + numero + " ya fue desbloqueada");
        }
        this.desbloqueado = true;
        this.regularizada = true;
        this.estado = EstadoAjusteContable.REGULARIZADA;
    }

    public boolean estaRegularizada() {
        return estado == EstadoAjusteContable.REGULARIZADA;
    }

    /**
     * Monto neto a pagar tras la regularización contable: el monto
     * facturado (Fase 01) menos la diferencia comercial ya regularizada
     * (Fase 03). Usado por la Fase 04 (Estrategia de Tesorería) para
     * preparar el lote de pagos.
     */
    public double getMontoNetoPagar() {
        return disputaComercial.getExcepcionFacturacion().getMontoFactura() - montoRegularizacion;
    }

    /**
     * Reconstrucción desde persistencia (usada por el mapper).
     */
    public static AjusteContableRegularizacion reconstruir(Long id, String numero, DisputaComercial disputaComercial,
                                                             double montoRegularizacion, Boolean recibeNotaCredito,
                                                             boolean reclamoGestionado, boolean notaCreditoEnviadaProveedor,
                                                             boolean notaCreditoRegistrada, boolean asientoRegularizacion,
                                                             boolean desbloqueado, boolean regularizada,
                                                             EstadoAjusteContable estado, LocalDateTime fecha) {
        AjusteContableRegularizacion ajuste = new AjusteContableRegularizacion();
        ajuste.id = id;
        ajuste.numero = numero;
        ajuste.disputaComercial = disputaComercial;
        ajuste.montoRegularizacion = montoRegularizacion;
        ajuste.recibeNotaCredito = recibeNotaCredito;
        ajuste.reclamoGestionado = reclamoGestionado;
        ajuste.notaCreditoEnviadaProveedor = notaCreditoEnviadaProveedor;
        ajuste.notaCreditoRegistrada = notaCreditoRegistrada;
        ajuste.asientoRegularizacion = asientoRegularizacion;
        ajuste.desbloqueado = desbloqueado;
        ajuste.regularizada = regularizada;
        ajuste.estado = estado;
        ajuste.fecha = fecha;
        return ajuste;
    }
}

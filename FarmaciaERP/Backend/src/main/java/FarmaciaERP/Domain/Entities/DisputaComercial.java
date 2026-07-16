package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Enums.EstadoDisputaComercial;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * FI-AP.02 Gestión Humana de Disputas Comerciales - Fase 02 (Workflow ERP):
 * el Comprador / Category Manager coteja la factura en excepción contra los
 * acuerdos comerciales, cuantifica el impacto financiero y negocia con el
 * laboratorio o droguería, por rondas, hasta acordar si la diferencia se
 * absorbe. La resolución registrada en el workflow del ERP habilita la
 * Fase 03 (Ajustes Contables y Regularización).
 */
@Getter
@Setter
public class DisputaComercial {

    private Long id;
    private String numero;
    private ExcepcionFacturacion excepcionFacturacion;
    private boolean cotejada;
    private boolean cuantificada;
    private double impactoFinanciero;
    private boolean validadaDesviacion;
    private boolean disputaAbierta;
    private int rondaNegociacion;
    private double montoContraoferta;
    private Boolean absorbeAceptado;
    private boolean resueltaWorkflow;
    private EstadoDisputaComercial estado;
    private LocalDateTime fecha;

    private DisputaComercial() {
    }

    /**
     * RN-AP2-01: la disputa comercial sólo puede abrirse a partir de una
     * excepción de facturación ya clasificada y notificada a Compras
     * (Fase 01 concluida).
     */
    public static DisputaComercial abrir(String numero, ExcepcionFacturacion excepcionFacturacion) {
        if (numero == null || numero.isBlank()) {
            throw new BadRequestException("El número de la disputa comercial es obligatorio");
        }
        if (excepcionFacturacion == null) {
            throw new BadRequestException(
                    "RN-AP2-01: la disputa comercial debe originarse de una excepción de facturación");
        }
        if (!excepcionFacturacion.estaNotificada()) {
            throw new BadRequestException(
                    "RN-AP2-01: la excepción " + excepcionFacturacion.getNumero()
                            + " debe estar clasificada y notificada a Compras antes de abrir la disputa comercial");
        }

        DisputaComercial disputa = new DisputaComercial();
        disputa.numero = numero;
        disputa.excepcionFacturacion = excepcionFacturacion;
        disputa.cotejada = false;
        disputa.cuantificada = false;
        disputa.impactoFinanciero = 0;
        disputa.validadaDesviacion = false;
        disputa.disputaAbierta = false;
        disputa.rondaNegociacion = 0;
        disputa.montoContraoferta = 0;
        disputa.absorbeAceptado = null;
        disputa.resueltaWorkflow = false;
        disputa.estado = EstadoDisputaComercial.EN_GESTION;
        disputa.fecha = LocalDateTime.now();
        return disputa;
    }

    /**
     * Paso 2.1.1 - Extracción y Cotejo de Datos de Facturación vs. Acuerdos.
     */
    public void cotejarFacturaContrato() {
        if (cotejada) {
            throw new BadRequestException("RN-AP2-02: la disputa " + numero + " ya fue cotejada contra el contrato");
        }
        this.cotejada = true;
    }

    /**
     * Paso 2.1.2 - Cuantificación del Impacto Financiero de la Desviación.
     * RN-AP2-03: el impacto se calcula sobre la diferencia detectada en la
     * excepción de facturación (Fase 01).
     */
    public void cuantificarImpactoFinanciero() {
        if (!cotejada) {
            throw new BadRequestException(
                    "RN-AP2-03: debe cotejarse la factura contra el contrato antes de cuantificar el impacto");
        }
        if (cuantificada) {
            throw new BadRequestException("RN-AP2-03: el impacto financiero ya fue cuantificado para " + numero);
        }
        this.impactoFinanciero = Math.abs(excepcionFacturacion.getDiferencia());
        this.cuantificada = true;
    }

    /**
     * Paso 2.1.3 - Revisión y Validación de la Desviación.
     */
    public void validarDesviacion() {
        if (!cuantificada) {
            throw new BadRequestException(
                    "RN-AP2-04: debe cuantificarse el impacto financiero antes de validar la desviación");
        }
        this.validadaDesviacion = true;
    }

    /**
     * Paso 2.2.1 - Apertura de Disputa con Ejecutivo de Droguería /
     * Laboratorio. Cada llamada abre una nueva ronda de negociación.
     */
    public void abrirNegociacion() {
        if (!validadaDesviacion) {
            throw new BadRequestException(
                    "RN-AP2-05: debe validarse la desviación antes de abrir la disputa con el proveedor");
        }
        if (resueltaWorkflow) {
            throw new BadRequestException("RN-AP2-05: la disputa " + numero + " ya fue resuelta");
        }
        if (disputaAbierta) {
            throw new BadRequestException(
                    "RN-AP2-05: ya existe una disputa abierta en la ronda " + rondaNegociacion + " para " + numero);
        }
        this.rondaNegociacion++;
        this.disputaAbierta = true;
    }

    /**
     * Paso 2.2.1 (cont.) - el laboratorio/droguería envía la contrapropuesta
     * de la ronda vigente.
     */
    public void registrarContraoferta(double montoContraoferta) {
        if (!disputaAbierta) {
            throw new BadRequestException(
                    "RN-AP2-06: debe abrirse la disputa con el proveedor antes de registrar su contrapropuesta");
        }
        if (montoContraoferta <= 0) {
            throw new BadRequestException("RN-AP2-06: el monto de la contrapropuesta debe ser mayor a cero");
        }
        if (montoContraoferta > impactoFinanciero) {
            throw new BadRequestException(
                    "RN-AP2-06: la contrapropuesta no puede superar el impacto financiero de " + impactoFinanciero);
        }
        this.montoContraoferta = montoContraoferta;
    }

    /**
     * Paso 2.2.2 - Evaluación de Contrapropuesta: el Comprador acepta que el
     * proveedor absorba la diferencia comercial acordada.
     */
    public void aceptarAbsorcion() {
        if (montoContraoferta <= 0) {
            throw new BadRequestException(
                    "RN-AP2-07: debe existir una contrapropuesta del proveedor antes de evaluarla");
        }
        this.absorbeAceptado = true;
    }

    /**
     * Paso 2.2.2 (rechazo) - la contrapropuesta es insuficiente: se reabre
     * la disputa para una nueva ronda de negociación.
     */
    public void reabrirNegociacion() {
        if (montoContraoferta <= 0) {
            throw new BadRequestException(
                    "RN-AP2-07: debe existir una contrapropuesta del proveedor antes de rechazarla");
        }
        if (Boolean.TRUE.equals(absorbeAceptado)) {
            throw new BadRequestException("RN-AP2-07: la absorción ya fue aceptada para " + numero);
        }
        this.absorbeAceptado = null;
        this.disputaAbierta = false;
        this.montoContraoferta = 0;
    }

    /**
     * Paso 2.3.1 - Registrar Resolución en Workflow del ERP. RN-AP2-08:
     * sólo puede resolverse una disputa cuya absorción haya sido aceptada,
     * habilitando la Fase 03 (Ajustes Contables y Regularización).
     */
    public void resolverWorkflow() {
        if (!Boolean.TRUE.equals(absorbeAceptado)) {
            throw new BadRequestException(
                    "RN-AP2-08: la absorción de la diferencia comercial debe estar aceptada antes de resolver el workflow");
        }
        if (resueltaWorkflow) {
            throw new BadRequestException("RN-AP2-08: la disputa " + numero + " ya fue resuelta");
        }
        this.resueltaWorkflow = true;
        this.estado = EstadoDisputaComercial.RESUELTA;
    }

    public boolean estaResuelta() {
        return estado == EstadoDisputaComercial.RESUELTA;
    }

    /**
     * Reconstrucción desde persistencia (usada por el mapper).
     */
    public static DisputaComercial reconstruir(Long id, String numero, ExcepcionFacturacion excepcionFacturacion,
                                                boolean cotejada, boolean cuantificada, double impactoFinanciero,
                                                boolean validadaDesviacion, boolean disputaAbierta, int rondaNegociacion,
                                                double montoContraoferta, Boolean absorbeAceptado,
                                                boolean resueltaWorkflow, EstadoDisputaComercial estado,
                                                LocalDateTime fecha) {
        DisputaComercial disputa = new DisputaComercial();
        disputa.id = id;
        disputa.numero = numero;
        disputa.excepcionFacturacion = excepcionFacturacion;
        disputa.cotejada = cotejada;
        disputa.cuantificada = cuantificada;
        disputa.impactoFinanciero = impactoFinanciero;
        disputa.validadaDesviacion = validadaDesviacion;
        disputa.disputaAbierta = disputaAbierta;
        disputa.rondaNegociacion = rondaNegociacion;
        disputa.montoContraoferta = montoContraoferta;
        disputa.absorbeAceptado = absorbeAceptado;
        disputa.resueltaWorkflow = resueltaWorkflow;
        disputa.estado = estado;
        disputa.fecha = fecha;
        return disputa;
    }
}

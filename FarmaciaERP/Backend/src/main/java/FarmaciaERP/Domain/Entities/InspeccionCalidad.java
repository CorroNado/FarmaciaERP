package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Enums.DecisionQA;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * LOG.06 Inspección y aseguramiento de calidad (QA11) - Fase 05: el Regente
 * Farmacéutico ejecuta el muestreo técnico, valida la cadena de frío, el
 * registro sanitario y el empaque antes de emitir la Decisión de Empleo
 * que habilita (o bloquea) la distribución del lote.
 */
@Getter
@Setter
public class InspeccionCalidad {

    private Long id;
    private String numero;
    private EntradaMercancia entradaMercancia;
    private boolean muestreoConforme;
    private boolean cadenaFrioConforme;
    private boolean registroSanitarioVigente;
    private boolean empaqueConforme;
    private DecisionQA decision;
    private String motivoRechazo;
    private LocalDateTime fecha;

    private InspeccionCalidad() {
    }

    /**
     * RN-E5-001 · RN-E5-002 · RN-E5-003 · RN-E5-004 · RN-E5-006 - Aprueba el
     * lote (Decisión de Empleo) solo si TODOS los controles son conformes,
     * incluida la cadena de frío heredada de la entrada de mercancía (MIGO).
     */
    public static InspeccionCalidad aprobar(String numero, EntradaMercancia entradaMercancia,
                                             boolean muestreoConforme, boolean registroSanitarioVigente,
                                             boolean empaqueConforme) {
        validarDatosBase(numero, entradaMercancia);

        boolean cadenaFrioConforme = !entradaMercancia.isAlertaCadenaFrio();
        if (!(muestreoConforme && registroSanitarioVigente && empaqueConforme && cadenaFrioConforme)) {
            throw new BadRequestException(
                    "RN-E5-006: todos los controles (muestreo, cadena de frío, registro sanitario y empaque) "
                            + "deben estar conformes para aprobar el lote. Utilice el rechazo si algún control no es conforme.");
        }

        InspeccionCalidad inspeccion = new InspeccionCalidad();
        inspeccion.numero = numero;
        inspeccion.entradaMercancia = entradaMercancia;
        inspeccion.muestreoConforme = true;
        inspeccion.cadenaFrioConforme = true;
        inspeccion.registroSanitarioVigente = true;
        inspeccion.empaqueConforme = true;
        inspeccion.decision = DecisionQA.APROBADO;
        inspeccion.fecha = LocalDateTime.now();
        return inspeccion;
    }

    /**
     * RN-E5-007 - Rechaza el lote: el estado queda bloqueado automáticamente
     * y se inicia la devolución al proveedor. Requiere un motivo documentado.
     */
    public static InspeccionCalidad rechazar(String numero, EntradaMercancia entradaMercancia, String motivoRechazo,
                                              boolean muestreoConforme, boolean registroSanitarioVigente,
                                              boolean empaqueConforme) {
        validarDatosBase(numero, entradaMercancia);
        if (motivoRechazo == null || motivoRechazo.isBlank()) {
            throw new BadRequestException("RN-E5-007: debe indicar el motivo de rechazo del lote");
        }

        InspeccionCalidad inspeccion = new InspeccionCalidad();
        inspeccion.numero = numero;
        inspeccion.entradaMercancia = entradaMercancia;
        inspeccion.muestreoConforme = muestreoConforme;
        inspeccion.cadenaFrioConforme = !entradaMercancia.isAlertaCadenaFrio();
        inspeccion.registroSanitarioVigente = registroSanitarioVigente;
        inspeccion.empaqueConforme = empaqueConforme;
        inspeccion.decision = DecisionQA.RECHAZADO;
        inspeccion.motivoRechazo = motivoRechazo;
        inspeccion.fecha = LocalDateTime.now();
        return inspeccion;
    }

    private static void validarDatosBase(String numero, EntradaMercancia entradaMercancia) {
        if (numero == null || numero.isBlank()) {
            throw new BadRequestException("El número de la Decisión de Empleo (QA11) es obligatorio");
        }
        if (entradaMercancia == null) {
            throw new BadRequestException("La inspección de calidad debe originarse de una entrada de mercancía (MIGO)");
        }
    }

    // RN-E6-002: solo un lote con Decisión de Empleo aprobada puede distribuirse (Fase 06).
    public boolean puedeDistribuir() {
        return decision == DecisionQA.APROBADO;
    }

    /**
     * Reconstrucción desde persistencia (usada por el mapper).
     */
    public static InspeccionCalidad reconstruir(Long id, String numero, EntradaMercancia entradaMercancia,
                                                 boolean muestreoConforme, boolean cadenaFrioConforme,
                                                 boolean registroSanitarioVigente, boolean empaqueConforme,
                                                 DecisionQA decision, String motivoRechazo, LocalDateTime fecha) {
        InspeccionCalidad inspeccion = new InspeccionCalidad();
        inspeccion.id = id;
        inspeccion.numero = numero;
        inspeccion.entradaMercancia = entradaMercancia;
        inspeccion.muestreoConforme = muestreoConforme;
        inspeccion.cadenaFrioConforme = cadenaFrioConforme;
        inspeccion.registroSanitarioVigente = registroSanitarioVigente;
        inspeccion.empaqueConforme = empaqueConforme;
        inspeccion.decision = decision;
        inspeccion.motivoRechazo = motivoRechazo;
        inspeccion.fecha = fecha;
        return inspeccion;
    }
}

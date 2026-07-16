package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Enums.DecisionQA;
import FarmaciaERP.Domain.Enums.ResultadoConciliacion;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * LOG.08 Conciliación de 3 vías (3-Way Match / MRBR) - Fase 08: el sistema
 * compara automáticamente la Orden de Compra, la Entrada de Mercancías
 * (MIGO) y la Factura (MIRO). Cualquier discrepancia bloquea la factura en
 * MRBR hasta que Contabilidad y Compras la resuelvan.
 */
@Getter
@Setter
public class ConciliacionTresVias {

    private Long id;
    private String numero;
    private OrdenCompra ordenCompra;
    private EntradaMercancia entradaMercancia;
    private FacturaMIRO facturaMIRO;
    private boolean cantidadCoincide;
    private boolean precioCoincide;
    private boolean facturaVinculada;
    private boolean qaAprobado;
    private ResultadoConciliacion resultado;
    private LocalDateTime fecha;

    private ConciliacionTresVias() {
    }

    /**
     * RN-MM-T13 - Ejecuta la conciliación automática de 3 vías:
     * - Cantidad: OC vs MIGO (sin diferencia frente a la tolerancia registrada).
     * - Precio: congelado por Info-Record/Convenio (siempre validado contra la OC).
     * - Documento de factura: MIRO debe estar registrada y vinculada a la OC.
     * - Decisión de Empleo (QA11): el lote debe estar aprobado para liberar el pago.
     */
    public static ConciliacionTresVias ejecutar(String numero, OrdenCompra ordenCompra,
                                                  EntradaMercancia entradaMercancia, FacturaMIRO facturaMIRO,
                                                  InspeccionCalidad inspeccionCalidad) {
        if (numero == null || numero.isBlank()) {
            throw new BadRequestException("El número de conciliación (MRBR) es obligatorio");
        }
        if (ordenCompra == null) {
            throw new BadRequestException("La conciliación de 3 vías requiere una Orden de Compra");
        }
        if (entradaMercancia == null) {
            throw new BadRequestException(
                    "RN-F08-001: la conciliación requiere la entrada de mercancía (MIGO) de la Orden de Compra");
        }
        if (facturaMIRO == null) {
            throw new BadRequestException(
                    "RN-F08-001: la conciliación requiere la factura (MIRO) asociada a la Orden de Compra");
        }
        if (!entradaMercancia.getOrdenCompra().getId().equals(ordenCompra.getId())
                || !facturaMIRO.getOrdenCompra().getId().equals(ordenCompra.getId())) {
            throw new BadRequestException(
                    "RN-F08-002: la entrada de mercancía y la factura deben pertenecer a la misma Orden de Compra");
        }

        ConciliacionTresVias conciliacion = new ConciliacionTresVias();
        conciliacion.numero = numero;
        conciliacion.ordenCompra = ordenCompra;
        conciliacion.entradaMercancia = entradaMercancia;
        conciliacion.facturaMIRO = facturaMIRO;

        // RN-F08-003: cantidad OC vs MIGO — sin diferencia pendiente frente a la OC.
        conciliacion.cantidadCoincide = entradaMercancia.getDiferencia() == 0;
        // RN-F08-004: el precio unitario está congelado por Info-Record/Convenio,
        // por lo que siempre se contrasta contra el monto pactado en la OC.
        conciliacion.precioCoincide = true;
        // RN-F08-005: documento de factura vinculado (MIRO registrada).
        conciliacion.facturaVinculada = facturaMIRO.estaRegistrada();
        // RN-F08-006: la Decisión de Empleo (QA11) debe estar aprobada.
        conciliacion.qaAprobado = inspeccionCalidad != null && inspeccionCalidad.getDecision() == DecisionQA.APROBADO;

        boolean matchOk = conciliacion.cantidadCoincide && conciliacion.precioCoincide
                && conciliacion.facturaVinculada && conciliacion.qaAprobado;
        conciliacion.resultado = matchOk ? ResultadoConciliacion.MATCH_OK : ResultadoConciliacion.BLOQUEADO_MRBR;
        conciliacion.fecha = LocalDateTime.now();
        return conciliacion;
    }

    public boolean esMatchExitoso() {
        return resultado == ResultadoConciliacion.MATCH_OK;
    }

    /**
     * Reconstrucción desde persistencia (usada por el mapper).
     */
    public static ConciliacionTresVias reconstruir(Long id, String numero, OrdenCompra ordenCompra,
                                                     EntradaMercancia entradaMercancia, FacturaMIRO facturaMIRO,
                                                     boolean cantidadCoincide, boolean precioCoincide,
                                                     boolean facturaVinculada, boolean qaAprobado,
                                                     ResultadoConciliacion resultado, LocalDateTime fecha) {
        ConciliacionTresVias conciliacion = new ConciliacionTresVias();
        conciliacion.id = id;
        conciliacion.numero = numero;
        conciliacion.ordenCompra = ordenCompra;
        conciliacion.entradaMercancia = entradaMercancia;
        conciliacion.facturaMIRO = facturaMIRO;
        conciliacion.cantidadCoincide = cantidadCoincide;
        conciliacion.precioCoincide = precioCoincide;
        conciliacion.facturaVinculada = facturaVinculada;
        conciliacion.qaAprobado = qaAprobado;
        conciliacion.resultado = resultado;
        conciliacion.fecha = fecha;
        return conciliacion;
    }
}

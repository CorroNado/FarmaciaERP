package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Enums.EstadoFacturaMIRO;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * LOG.07 Verificación de factura (MIRO) - Fase 07: se recibe la factura
 * electrónica del proveedor, se registra en el sistema y se calcula el IGV
 * sobre el monto neto de la Orden de Compra, quedando asociada a esta para
 * su posterior conciliación de 3 vías (Fase 08).
 */
@Getter
@Setter
public class FacturaMIRO {

    private static final double TASA_IGV = 0.18;

    private Long id;
    private String numero;
    private String numeroFactura;
    private OrdenCompra ordenCompra;
    private String fechaEmision;
    private double montoNeto;
    private double igv;
    private double montoTotal;
    private EstadoFacturaMIRO estado;
    private LocalDateTime fecha;

    public FacturaMIRO() {
    }

    /**
     * RN: MM-T12 - Verificación de factura. La factura solo puede registrarse
     * contra una Orden de Compra firmada y despachada, que ya cuente con su
     * entrada de mercancía (MIGO) registrada, y no debe existir otra factura
     * MIRO ya asociada a la misma OC.
     */
    public FacturaMIRO(String numero, String numeroFactura, OrdenCompra ordenCompra, String fechaEmision) {
        if (numero == null || numero.isBlank()) {
            throw new BadRequestException("El número de registro MIRO es obligatorio");
        }
        // RN-F07-001: el número de factura del proveedor es obligatorio.
        if (numeroFactura == null || numeroFactura.isBlank()) {
            throw new BadRequestException("RN-F07-001: el N° de factura es obligatorio");
        }
        if (ordenCompra == null) {
            throw new BadRequestException("La factura MIRO debe asociarse a una Orden de Compra");
        }
        // RN-F07-002: la OC debe estar firmada y despachada antes de facturar.
        if (!ordenCompra.estaFirmada()) {
            throw new BadRequestException(
                    "RN-F07-002: la Orden de Compra debe estar firmada y despachada antes de registrar la factura (MIRO)");
        }
        if (fechaEmision == null || fechaEmision.isBlank()) {
            throw new BadRequestException("RN-F07-001: la fecha de emisión de la factura es obligatoria");
        }

        this.numero = numero;
        this.numeroFactura = numeroFactura;
        this.ordenCompra = ordenCompra;
        this.fechaEmision = fechaEmision;

        // RN-F07-003: el monto neto se calcula sobre los ítems de la OC y el
        // IGV se aplica a la tasa vigente (18%).
        this.montoNeto = ordenCompra.getMontoTotal();
        this.igv = montoNeto * TASA_IGV;
        this.montoTotal = montoNeto + igv;

        this.estado = EstadoFacturaMIRO.REGISTRADA;
        this.fecha = LocalDateTime.now();
    }

    public boolean estaRegistrada() {
        return estado == EstadoFacturaMIRO.REGISTRADA;
    }
}

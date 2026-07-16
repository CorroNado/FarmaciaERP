package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Enums.EstadoEntradaMercancia;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * LOG.05 Entrada de Mercancía (MIGO) - Fase 04: se verifica la cantidad
 * física contra la Orden de Compra, se registra lote y vencimiento, y se
 * valida la cadena de frío antes de mover el material a cuarentena
 * (Fase 05 - Inspección de calidad).
 */
@Getter
@Setter
public class EntradaMercancia {

    private static final double TOLERANCIA_DIFERENCIA_PORCENTUAL = 2.0;
    private static final double TEMP_MINIMA_CADENA_FRIO = 2.0;
    private static final double TEMP_MAXIMA_CADENA_FRIO = 8.0;

    private Long id;
    private String numero;
    private OrdenCompra ordenCompra;
    private String lote;
    private String fechaVencimiento;
    private double temperaturaArribo;
    private int cantidadPedida;
    private int cantidadRecibida;
    private LocalDateTime fecha;
    private EstadoEntradaMercancia estado;
    private boolean alertaCadenaFrio;

    public EntradaMercancia() {
    }

    public EntradaMercancia(String numero, OrdenCompra ordenCompra, String lote, String fechaVencimiento,
                             double temperaturaArribo, int cantidadRecibida, boolean confirmarExcepcion) {
        if (numero == null || numero.isBlank()) {
            throw new BadRequestException("El número de entrada de mercancía es obligatorio");
        }
        if (ordenCompra == null) {
            throw new BadRequestException("La entrada de mercancía debe originarse de una Orden de Compra");
        }
        if (!ordenCompra.estaFirmada()) {
            throw new BadRequestException(
                    "RN-F04-002: la Orden de Compra debe estar firmada y despachada antes de registrar la entrada");
        }
        // RN-F04-013: lote y vencimiento son obligatorios para la trazabilidad.
        if (lote == null || lote.isBlank()) {
            throw new BadRequestException("RN-F04-013: el número de lote es obligatorio");
        }
        if (fechaVencimiento == null || fechaVencimiento.isBlank()) {
            throw new BadRequestException("RN-F04-013: la fecha de vencimiento es obligatoria");
        }
        if (cantidadRecibida < 0) {
            throw new BadRequestException("La cantidad recibida no puede ser negativa");
        }

        this.numero = numero;
        this.ordenCompra = ordenCompra;
        this.lote = lote;
        this.fechaVencimiento = fechaVencimiento;
        this.temperaturaArribo = temperaturaArribo;
        this.cantidadPedida = ordenCompra.getDetalles().stream()
                .mapToInt(DetalleOrdenCompra::getCantidad)
                .sum();
        this.cantidadRecibida = cantidadRecibida;
        this.fecha = LocalDateTime.now();

        // RN-F04-001 / RN-F04-003: conformidad de cantidad contra la OC.
        double pctDiferencia = getPorcentajeDiferencia();
        if (pctDiferencia > TOLERANCIA_DIFERENCIA_PORCENTUAL) {
            if (!confirmarExcepcion) {
                throw new BadRequestException(String.format(
                        "RN-F04-003: la diferencia de %d unidades (%.1f%%) supera el 2%% permitido frente a la Orden de Compra. "
                                + "Confirme la excepción documentada para notificar al proveedor y continuar.",
                        Math.abs(getDiferencia()), pctDiferencia));
            }
            this.estado = EstadoEntradaMercancia.SUSPENDIDA;
        } else {
            this.estado = EstadoEntradaMercancia.REGISTRADA;
        }

        // RN-F04-014: validación de cadena de frío (2–8 °C).
        this.alertaCadenaFrio = temperaturaArribo < TEMP_MINIMA_CADENA_FRIO
                || temperaturaArribo > TEMP_MAXIMA_CADENA_FRIO;
    }

    public int getDiferencia() {
        return cantidadRecibida - cantidadPedida;
    }

    public double getPorcentajeDiferencia() {
        if (cantidadPedida == 0) return 0.0;
        return Math.abs(getDiferencia()) / (double) cantidadPedida * 100.0;
    }

    public boolean requiereRevisionEstrictaQA() {
        return alertaCadenaFrio;
    }
}

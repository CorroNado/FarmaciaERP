package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Enums.EstadoOrdenCompra;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * LOG.03 Orden de Compra (OC / PO, ME21N) - Fase 03: tratamiento y emisión
 * de la orden de compra hacia el proveedor.
 */
@Getter
@Setter
public class OrdenCompra {
    private Long id;
    private String numero;
    private SolicitudPedido solicitudPedido;
    private Proveedor proveedor;
    private Convenio convenio;
    private List<DetalleOrdenCompra> detalles = new ArrayList<>();
    private LocalDateTime fecha;
    private String fechaEntregaLimite;
    private String centroDestino;
    private EstadoOrdenCompra estado;
    private LocalDateTime fechaFirma;

    public OrdenCompra() {
    }

    public OrdenCompra(String numero, SolicitudPedido solicitudPedido, Proveedor proveedor, Convenio convenio,
                        List<DetalleOrdenCompra> detalles, String centroDestino) {
        if (numero == null || numero.isBlank()) {
            throw new BadRequestException("El número de Orden de Compra es obligatorio");
        }
        if (solicitudPedido == null) {
            throw new BadRequestException("La Orden de Compra debe originarse de una SolPed");
        }
        if (proveedor == null || convenio == null) {
            throw new BadRequestException(
                    "RN-OC-002: la Orden de Compra requiere proveedor y convenio con precios heredados");
        }
        if (detalles == null || detalles.isEmpty()) {
            throw new BadRequestException("La Orden de Compra debe tener al menos un ítem");
        }
        this.numero = numero;
        this.solicitudPedido = solicitudPedido;
        this.proveedor = proveedor;
        this.convenio = convenio;
        this.detalles = new ArrayList<>(detalles);
        this.centroDestino = centroDestino;
        this.fecha = LocalDateTime.now();
        this.estado = EstadoOrdenCompra.BORRADOR;
    }

    public double getMontoTotal() {
        return detalles.stream().mapToDouble(DetalleOrdenCompra::getSubtotal).sum();
    }

    /**
     * Fase 03 (RN-OC-003, RN-OC-006, RN-OC-008) - Firma digital: exige fecha
     * límite de entrega y despacha electrónicamente la OC al proveedor.
     */
    public void firmar(String fechaEntregaLimite) {
        if (estado != EstadoOrdenCompra.BORRADOR) {
            throw new BadRequestException("Solo una Orden de Compra en borrador puede firmarse");
        }
        // RN-OC-003: la fecha límite de entrega es obligatoria.
        if (fechaEntregaLimite == null || fechaEntregaLimite.isBlank()) {
            throw new BadRequestException("RN-OC-003: la fecha límite de entrega es obligatoria");
        }
        this.fechaEntregaLimite = fechaEntregaLimite;
        this.estado = EstadoOrdenCompra.FIRMADA;
        this.fechaFirma = LocalDateTime.now();
    }

    public boolean estaFirmada() {
        return estado == EstadoOrdenCompra.FIRMADA;
    }
}

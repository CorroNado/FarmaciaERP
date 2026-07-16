package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Enums.EstadoPago;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * LOG.09 Gestión y ejecución del pago (F110) - Fase 09: se programa el flujo
 * de caja, se libera la factura aprobada para pago (conciliada sin
 * discrepancias en el 3-way match), se ejecuta la transferencia bancaria y
 * se cierra contablemente la cuenta por pagar (FIPO).
 */
@Getter
@Setter
public class Pago {

    private Long id;
    private String numero;
    private FacturaMIRO facturaMIRO;
    private ConciliacionTresVias conciliacionTresVias;
    private String banco;
    private String fechaPago;
    private double monto;
    private EstadoPago estado;
    private LocalDateTime fecha;

    public Pago() {
    }

    /**
     * RN: Ejecución del pago (F110). Solo puede ejecutarse el pago de una
     * factura cuya conciliación de 3 vías haya resultado en Match exitoso;
     * de lo contrario la factura permanece bloqueada en MRBR.
     */
    public Pago(String numero, FacturaMIRO facturaMIRO, ConciliacionTresVias conciliacionTresVias,
                String banco, String fechaPago) {
        if (numero == null || numero.isBlank()) {
            throw new BadRequestException("El número de pago es obligatorio");
        }
        if (facturaMIRO == null) {
            throw new BadRequestException("El pago debe asociarse a una factura (MIRO)");
        }
        if (conciliacionTresVias == null) {
            throw new BadRequestException(
                    "RN-F09-001: la factura debe contar con una conciliación de 3 vías (MRBR) antes de programar el pago");
        }
        // RN-F09-002: solo se libera el pago si el 3-way match fue exitoso.
        if (!conciliacionTresVias.esMatchExitoso()) {
            throw new BadRequestException(
                    "RN-F09-002: la factura está bloqueada en MRBR por discrepancias en el 3-way match; "
                            + "no puede liberarse para pago hasta que Contabilidad y Compras la resuelvan");
        }
        // RN-F09-003: banco/cuenta de origen es obligatorio para ejecutar la transferencia.
        if (banco == null || banco.isBlank()) {
            throw new BadRequestException("RN-F09-003: el banco/cuenta de origen es obligatorio");
        }
        if (fechaPago == null || fechaPago.isBlank()) {
            throw new BadRequestException("RN-F09-003: la fecha de pago es obligatoria");
        }

        this.numero = numero;
        this.facturaMIRO = facturaMIRO;
        this.conciliacionTresVias = conciliacionTresVias;
        this.banco = banco;
        this.fechaPago = fechaPago;
        this.monto = facturaMIRO.getMontoTotal();
        this.estado = EstadoPago.EJECUTADO;
        this.fecha = LocalDateTime.now();
    }

    public boolean estaEjecutado() {
        return estado == EstadoPago.EJECUTADO;
    }
}

package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Enums.EstadoProveedor;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import lombok.Getter;
import lombok.Setter;

/**
 * LOG.00 - Maestro de proveedores (laboratorios) homologados para el
 * proceso Procure-to-Pay.
 */
@Getter
@Setter
public class Proveedor {
    private Long id;
    private String razonSocial;
    private String ruc;
    private String contactoEmail;
    private String contactoTelefono;
    private EstadoProveedor estado;

    public Proveedor() {
    }

    public Proveedor(String razonSocial, String ruc, String contactoEmail, String contactoTelefono) {
        if (razonSocial == null || razonSocial.isBlank()) {
            throw new BadRequestException("La razón social del proveedor es obligatoria");
        }
        if (ruc == null || ruc.isBlank()) {
            throw new BadRequestException("El RUC del proveedor es obligatorio");
        }
        this.razonSocial = razonSocial;
        this.ruc = ruc;
        this.contactoEmail = contactoEmail;
        this.contactoTelefono = contactoTelefono;
        this.estado = EstadoProveedor.ACTIVO;
    }

    public boolean estaActivo() {
        return estado == EstadoProveedor.ACTIVO;
    }
}

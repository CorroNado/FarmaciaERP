package FarmaciaERP.Domain.Entities;

import FarmaciaERP.Domain.Enums.EstadoConvenio;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * LOG.02 - Contrato Marco (ME33K) que respalda la determinación de la
 * fuente de aprovisionamiento (Fase 02, RN-MM-001).
 */
@Getter
@Setter
public class Convenio {
    private Long id;
    private String numero;
    private Proveedor proveedor;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private EstadoConvenio estado;
    private List<ItemConvenio> itemsPactados = new ArrayList<>();

    public Convenio() {
    }

    public Convenio(String numero, Proveedor proveedor, LocalDate fechaInicio, LocalDate fechaFin,
                     List<ItemConvenio> itemsPactados) {
        if (numero == null || numero.isBlank()) {
            throw new BadRequestException("El número de contrato marco es obligatorio");
        }
        if (proveedor == null) {
            throw new BadRequestException("El convenio debe estar asociado a un proveedor");
        }
        if (fechaInicio == null || fechaFin == null || fechaFin.isBefore(fechaInicio)) {
            throw new BadRequestException("La vigencia del convenio es inválida");
        }
        this.numero = numero;
        this.proveedor = proveedor;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.itemsPactados = itemsPactados != null ? new ArrayList<>(itemsPactados) : new ArrayList<>();
        this.estado = EstadoConvenio.VIGENTE;
    }

    /**
     * RN-MM-001 - Un convenio solo respalda una fuente de aprovisionamiento
     * mientras esté dentro de su vigencia y no haya sido suspendido.
     */
    public boolean estaVigente() {
        LocalDate hoy = LocalDate.now();
        return estado == EstadoConvenio.VIGENTE
                && !hoy.isBefore(fechaInicio)
                && !hoy.isAfter(fechaFin);
    }

    /**
     * RN-MM-004 - Devuelve el precio congelado (Info-Record) para un
     * medicamento dentro de este convenio, si existe.
     */
    public Optional<Double> precioPactadoPara(int medicamentoId) {
        return itemsPactados.stream()
                .filter(item -> item.getMedicamento().getId() == medicamentoId)
                .map(ItemConvenio::getPrecioPactado)
                .findFirst();
    }

    public void suspender() {
        this.estado = EstadoConvenio.SUSPENDIDO;
    }
}

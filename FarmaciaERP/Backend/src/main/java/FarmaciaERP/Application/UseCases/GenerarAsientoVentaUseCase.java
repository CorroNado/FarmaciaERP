package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Domain.Constants.CuentasContables;
import FarmaciaERP.Domain.Entities.AsientoContable;
import FarmaciaERP.Domain.Entities.LineaAsiento;
import FarmaciaERP.Domain.Entities.SubcuentaDivisionaria;
import FarmaciaERP.Domain.Entities.Venta;
import FarmaciaERP.Domain.Enums.TipoAsiento;
import FarmaciaERP.Domain.Exceptions.BadRequestException;
import FarmaciaERP.Domain.Repositories.IAsientoContableRepository;
import FarmaciaERP.Domain.Repositories.ISubcuentaDivisionariaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

/**
 * Genera automaticamente los asientos contables del ciclo de Ventas.
 * Venta.getTotal() es NETO (no incluye IGV):
 *
 *  1) generarAsientoVenta - al confirmar la venta (SD.03):
 *     Debe Clientes (total con IGV) / Haber Ventas (neto) + Haber IGV.
 *  2) generarAsientoCobro - al registrar el cobro (SD.04):
 *     Debe Caja (total con IGV) / Haber Clientes (total con IGV).
 *
 * Idempotente por numero de asiento: si ya existe uno con ese numero
 * (VTA-{id} / VTA-COBRO-{id}), no genera un duplicado.
 */
@Service
public class GenerarAsientoVentaUseCase {

    private static final BigDecimal TASA_IGV = BigDecimal.valueOf(0.18);

    private final IAsientoContableRepository asientoRepository;
    private final ISubcuentaDivisionariaRepository subcuentaRepository;

    public GenerarAsientoVentaUseCase(IAsientoContableRepository asientoRepository,
                                       ISubcuentaDivisionariaRepository subcuentaRepository) {
        this.asientoRepository = asientoRepository;
        this.subcuentaRepository = subcuentaRepository;
    }

    @Transactional
    public void generarAsientoVenta(Venta venta) {
        String numero = "VTA-" + venta.getId();
        if (asientoRepository.existsByNumero(numero)) {
            return;
        }

        BigDecimal neto = BigDecimal.valueOf(venta.getTotal()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal igv = neto.multiply(TASA_IGV).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = neto.add(igv);

        SubcuentaDivisionaria clientes = buscarSubcuenta(CuentasContables.CLIENTES);
        SubcuentaDivisionaria ventas = buscarSubcuenta(CuentasContables.VENTAS);
        SubcuentaDivisionaria igvCuenta = buscarSubcuenta(CuentasContables.IGV);

        List<LineaAsiento> lineas = List.of(
                new LineaAsiento(clientes, null, total, BigDecimal.ZERO,
                        "Venta N. " + venta.getId()),
                new LineaAsiento(ventas, null, BigDecimal.ZERO, neto,
                        "Venta N. " + venta.getId()),
                new LineaAsiento(igvCuenta, null, BigDecimal.ZERO, igv,
                        "IGV Venta N. " + venta.getId())
        );

        AsientoContable asiento = new AsientoContable(
                numero, LocalDate.now(),
                "Venta N. " + venta.getId() + " - Cliente ID " + venta.getCliente().getId(),
                TipoAsiento.INGRESO, lineas
        );
        asiento.contabilizar();
        asientoRepository.save(asiento);
    }

    @Transactional
    public void generarAsientoCobro(Venta venta) {
        String numero = "VTA-COBRO-" + venta.getId();
        if (asientoRepository.existsByNumero(numero)) {
            return;
        }

        BigDecimal neto = BigDecimal.valueOf(venta.getTotal()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal igv = neto.multiply(TASA_IGV).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = neto.add(igv);

        SubcuentaDivisionaria caja = buscarSubcuenta(CuentasContables.CAJA);
        SubcuentaDivisionaria clientes = buscarSubcuenta(CuentasContables.CLIENTES);

        List<LineaAsiento> lineas = List.of(
                new LineaAsiento(caja, null, total, BigDecimal.ZERO,
                        "Cobro Venta N. " + venta.getId()),
                new LineaAsiento(clientes, null, BigDecimal.ZERO, total,
                        "Cobro Venta N. " + venta.getId())
        );

        AsientoContable asiento = new AsientoContable(
                numero, LocalDate.now(),
                "Cobro Venta N. " + venta.getId(),
                TipoAsiento.INGRESO, lineas
        );
        asiento.contabilizar();
        asientoRepository.save(asiento);
    }

    private SubcuentaDivisionaria buscarSubcuenta(String codigo) {
        return subcuentaRepository.findByCodigo(codigo)
                .orElseThrow(() -> new BadRequestException(
                        "Subcuenta contable no encontrada: " + codigo + ". Verifica que el Plan de Cuentas este cargado."));
    }
}
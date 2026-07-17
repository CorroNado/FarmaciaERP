package FarmaciaERP.Application.UseCases;

import FarmaciaERP.Domain.Constants.CuentasContables;
import FarmaciaERP.Domain.Entities.AsientoContable;
import FarmaciaERP.Domain.Entities.EntradaMercancia;
import FarmaciaERP.Domain.Entities.FacturaMIRO;
import FarmaciaERP.Domain.Entities.LineaAsiento;
import FarmaciaERP.Domain.Entities.SubcuentaDivisionaria;
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
 * Genera automaticamente los asientos contables del ciclo de Compras
 * (metodo GR/IR de 2 pasos, igual que el flujo MIGO/MIRO ya implementado):
 *
 *  1) generarAsientoRecepcion - al registrar la entrada de mercancia (MIGO):
 *     Debe Mercaderias / Haber Mercaderia por Facturar (cuenta puente),
 *     por el monto neto de la Orden de Compra (la factura aun no llega,
 *     no hay IGV todavia).
 *
 *  2) generarAsientoFactura - al registrar la factura del proveedor (MIRO):
 *     Debe Mercaderia por Facturar (cancela la cuenta puente) + Debe IGV
 *     (credito fiscal) / Haber Proveedores (monto total con IGV).
 *
 * Idempotente por numero de asiento (MIGO-ASIENTO-{id} / MIRO-ASIENTO-{id}).
 */
@Service
public class GenerarAsientoCompraUseCase {

    private final IAsientoContableRepository asientoRepository;
    private final ISubcuentaDivisionariaRepository subcuentaRepository;

    public GenerarAsientoCompraUseCase(IAsientoContableRepository asientoRepository,
                                        ISubcuentaDivisionariaRepository subcuentaRepository) {
        this.asientoRepository = asientoRepository;
        this.subcuentaRepository = subcuentaRepository;
    }

    @Transactional
    public void generarAsientoRecepcion(EntradaMercancia entrada) {
        String numero = "MIGO-ASIENTO-" + entrada.getId();
        if (asientoRepository.existsByNumero(numero)) {
            return;
        }

        BigDecimal montoNeto = BigDecimal.valueOf(entrada.getOrdenCompra().getMontoTotal())
                .setScale(2, RoundingMode.HALF_UP);

        SubcuentaDivisionaria mercaderias = buscarSubcuenta(CuentasContables.MERCADERIAS);
        SubcuentaDivisionaria porFacturar = buscarSubcuenta(CuentasContables.MERCADERIA_POR_FACTURAR);

        List<LineaAsiento> lineas = List.of(
                new LineaAsiento(mercaderias, null, montoNeto, BigDecimal.ZERO,
                        "Entrada mercancia (MIGO) N. " + entrada.getNumero()),
                new LineaAsiento(porFacturar, null, BigDecimal.ZERO, montoNeto,
                        "Entrada mercancia (MIGO) N. " + entrada.getNumero())
        );

        AsientoContable asiento = new AsientoContable(
                numero, LocalDate.now(),
                "Entrada mercancia (MIGO) " + entrada.getNumero() + " - OC " + entrada.getOrdenCompra().getNumero(),
                TipoAsiento.EGRESO, lineas
        );
        asiento.contabilizar();
        asientoRepository.save(asiento);
    }

    @Transactional
    public void generarAsientoFactura(FacturaMIRO factura) {
        String numero = "MIRO-ASIENTO-" + factura.getId();
        if (asientoRepository.existsByNumero(numero)) {
            return;
        }

        BigDecimal montoNeto = BigDecimal.valueOf(factura.getMontoNeto()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal igv = BigDecimal.valueOf(factura.getIgv()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal montoTotal = BigDecimal.valueOf(factura.getMontoTotal()).setScale(2, RoundingMode.HALF_UP);

        SubcuentaDivisionaria porFacturar = buscarSubcuenta(CuentasContables.MERCADERIA_POR_FACTURAR);
        SubcuentaDivisionaria igvCuenta = buscarSubcuenta(CuentasContables.IGV);
        SubcuentaDivisionaria proveedores = buscarSubcuenta(CuentasContables.PROVEEDORES);

        List<LineaAsiento> lineas = List.of(
                new LineaAsiento(porFacturar, null, montoNeto, BigDecimal.ZERO,
                        "Factura (MIRO) N. " + factura.getNumeroFactura()),
                new LineaAsiento(igvCuenta, null, igv, BigDecimal.ZERO,
                        "IGV Factura (MIRO) N. " + factura.getNumeroFactura()),
                new LineaAsiento(proveedores, null, BigDecimal.ZERO, montoTotal,
                        "Factura (MIRO) N. " + factura.getNumeroFactura())
        );

        AsientoContable asiento = new AsientoContable(
                numero, LocalDate.now(),
                "Factura (MIRO) " + factura.getNumeroFactura() + " - OC " + factura.getOrdenCompra().getNumero(),
                TipoAsiento.EGRESO, lineas
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
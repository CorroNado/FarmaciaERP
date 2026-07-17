package FarmaciaERP.Infrastucture.Config;

import FarmaciaERP.Domain.Constants.CuentasContables;
import FarmaciaERP.Domain.Entities.Cuenta;
import FarmaciaERP.Domain.Entities.SubcuentaDivisionaria;
import FarmaciaERP.Domain.Enums.NaturalezaCuenta;
import FarmaciaERP.Domain.Enums.TipoCuenta;
import FarmaciaERP.Domain.Repositories.ICuentaRepository;
import FarmaciaERP.Domain.Repositories.ISubcuentaDivisionariaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Carga el Plan de Cuentas base (PCGE) necesario para que los asientos
 * automaticos de ventas y compras puedan resolver sus subcuentas.
 * Idempotente: si una cuenta o subcuenta ya existe (por codigo), no la
 * vuelve a crear.
 */
@Component
public class PlanContableSeeder implements CommandLineRunner {

    private final ICuentaRepository cuentaRepository;
    private final ISubcuentaDivisionariaRepository subcuentaRepository;

    public PlanContableSeeder(ICuentaRepository cuentaRepository,
                               ISubcuentaDivisionariaRepository subcuentaRepository) {
        this.cuentaRepository = cuentaRepository;
        this.subcuentaRepository = subcuentaRepository;
    }

    @Override
    public void run(String... args) {
        Cuenta caja = asegurarCuenta("10", "Caja y Bancos", TipoCuenta.ACTIVO, NaturalezaCuenta.DEUDORA);
        Cuenta clientes = asegurarCuenta("12", "Cuentas por Cobrar Comerciales - Terceros", TipoCuenta.ACTIVO, NaturalezaCuenta.DEUDORA);
        Cuenta mercaderias = asegurarCuenta("20", "Mercaderias", TipoCuenta.ACTIVO, NaturalezaCuenta.DEUDORA);
        Cuenta tributos = asegurarCuenta("40", "Tributos y Aportes al Sistema de Pensiones y de Salud por Pagar", TipoCuenta.PASIVO, NaturalezaCuenta.ACREEDORA);
        Cuenta proveedores = asegurarCuenta("42", "Cuentas por Pagar Comerciales - Terceros", TipoCuenta.PASIVO, NaturalezaCuenta.ACREEDORA);
        Cuenta compras = asegurarCuenta("60", "Compras", TipoCuenta.GASTO, NaturalezaCuenta.DEUDORA);
        Cuenta ventas = asegurarCuenta("70", "Ventas", TipoCuenta.INGRESO, NaturalezaCuenta.ACREEDORA);

        asegurarSubcuenta(CuentasContables.CAJA, "Caja", caja);
        asegurarSubcuenta(CuentasContables.CLIENTES, "Emitidas en Cartera", clientes);
        asegurarSubcuenta(CuentasContables.MERCADERIAS, "Mercaderias Manufacturadas", mercaderias);
        asegurarSubcuenta(CuentasContables.IGV, "IGV - Cuenta Propia", tributos);
        asegurarSubcuenta(CuentasContables.MERCADERIA_POR_FACTURAR, "Mercaderia por Facturar (GR/IR)", proveedores);
        asegurarSubcuenta(CuentasContables.PROVEEDORES, "Facturas, Boletas y Otros Comprobantes por Pagar - Emitidas", proveedores);
        asegurarSubcuenta(CuentasContables.COMPRAS, "Mercaderias", compras);
        asegurarSubcuenta(CuentasContables.VENTAS, "Mercaderias - Terceros", ventas);
    }

    private Cuenta asegurarCuenta(String codigo, String nombre, TipoCuenta tipo, NaturalezaCuenta naturaleza) {
        return cuentaRepository.findByCodigo(codigo)
                .orElseGet(() -> cuentaRepository.save(new Cuenta(codigo, nombre, tipo, naturaleza)));
    }

    private void asegurarSubcuenta(String codigo, String nombre, Cuenta cuenta) {
        if (subcuentaRepository.findByCodigo(codigo).isEmpty()) {
            subcuentaRepository.save(new SubcuentaDivisionaria(codigo, nombre, cuenta));
        }
    }
}
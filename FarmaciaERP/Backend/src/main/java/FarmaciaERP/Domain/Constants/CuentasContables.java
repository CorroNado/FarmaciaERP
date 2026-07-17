package FarmaciaERP.Domain.Constants;

/**
 * Codigos de SubcuentaDivisionaria (PCGE) usados por los asientos
 * automaticos de Ventas y Compras. Deben existir en el Plan de Cuentas
 * (ver PlanContableSeeder) antes de generar cualquier asiento.
 */
public final class CuentasContables {

    private CuentasContables() {
    }

    // Ventas / cobranza
    public static final String CAJA = "1011";
    public static final String CLIENTES = "1212";
    public static final String VENTAS = "7011";

    // Compras
    public static final String MERCADERIAS = "2011";
    public static final String MERCADERIA_POR_FACTURAR = "4212";
    public static final String PROVEEDORES = "4211";
    public static final String COMPRAS = "6011";

    // Compartida
    public static final String IGV = "4011";
}
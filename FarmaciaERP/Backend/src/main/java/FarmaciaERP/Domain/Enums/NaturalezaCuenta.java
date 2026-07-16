package FarmaciaERP.Domain.Enums;

/**
 * Naturaleza contable: define si la cuenta aumenta con el DEBE (deudora)
 * o con el HABER (acreedora). Se usa para validar la cuadratura de los
 * asientos del Libro Diario.
 */
public enum NaturalezaCuenta {
    DEUDORA,
    ACREEDORA
}
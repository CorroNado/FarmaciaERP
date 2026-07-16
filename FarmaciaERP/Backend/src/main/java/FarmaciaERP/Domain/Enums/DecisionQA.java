package FarmaciaERP.Domain.Enums;

public enum DecisionQA {
    APROBADO("Libre utilización"),
    RECHAZADO("Bloqueado — devolución iniciada");

    private final String descripcion;

    DecisionQA(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}

package FarmaciaERP.Domain.Enums;

public enum EstadoDisputaComercial {
    EN_GESTION("En gestión — Comprador cotejando, cuantificando y negociando la desviación"),
    RESUELTA("Resuelta en el workflow del ERP — apta para Fase 03");

    private final String descripcion;

    EstadoDisputaComercial(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}

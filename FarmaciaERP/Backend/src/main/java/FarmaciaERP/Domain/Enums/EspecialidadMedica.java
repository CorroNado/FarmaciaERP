package FarmaciaERP.Domain.Enums;

public enum EspecialidadMedica {
    MEDICINA_GENERAL("Medicina General"),
    CARDIOLOGIA("Cardiologia"),
    PEDIATRIA("Pediatria"),
    DERMATOLOGIA("Dermatologia"),
    GINECOLOGIA("Ginecologia"),
    TRAUMATOLOGIA("Traumatologia"),
    NEUROLOGIA("Neurologia"),
    PSIQUIATRIA("Psiquiatria"),
    ODONTOLOGIA("Odontologia"),
    OTRO("Otro");
    private final String descripcion;
    EspecialidadMedica(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}

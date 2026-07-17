package FarmaciaERP.Domain.Enums;

/**
 * RRHH.02 - Control de Asistencia: turnos laborales disponibles, con los
 * parámetros de puntualidad, duración y cupo usados por el prototipo
 * (shiftRanges / shiftCapacity / getShiftDuration).
 */
public enum TurnoAsistencia {
    MANANA("Mañana", 7 * 60, 8, false, 1),
    TARDE("Tarde", 15 * 60, 8, false, 2),
    NOCHE_GUARDIA("Noche / Guardia", 23 * 60, 8, true, 1),
    MEDIO_TIEMPO("Medio Tiempo", 8 * 60, 4, false, 1),
    APOYO_REFUERZO("Apoyo / Refuerzo", 18 * 60, 4, false, 1);

    /** RN-AST-05: margen para considerar "A tiempo" (minutos desde el inicio del turno). */
    private static final int MARGEN_A_TIEMPO_MIN = 15;
    /** RN-AST-05: margen para considerar "Tardanza" (minutos desde el inicio del turno). */
    private static final int MARGEN_TARDANZA_MIN = 30;

    private final String descripcion;
    private final int horaInicioMinutos;
    private final int duracionHoras;
    private final boolean nocturno;
    private final int cupoMaximo;

    TurnoAsistencia(String descripcion, int horaInicioMinutos, int duracionHoras, boolean nocturno, int cupoMaximo) {
        this.descripcion = descripcion;
        this.horaInicioMinutos = horaInicioMinutos;
        this.duracionHoras = duracionHoras;
        this.nocturno = nocturno;
        this.cupoMaximo = cupoMaximo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getHoraInicioMinutos() {
        return horaInicioMinutos;
    }

    public int getDuracionHoras() {
        return duracionHoras;
    }

    public boolean isNocturno() {
        return nocturno;
    }

    public int getCupoMaximo() {
        return cupoMaximo;
    }

    public boolean esTurnoLargo() {
        return duracionHoras >= 8;
    }

    /** RN-AST-06: 2.0x nocturno (Noche / Guardia), 1.5x el resto de turnos. */
    public double getFactorHoraExtra() {
        return nocturno ? 2.0 : 1.5;
    }

    public int getFinATiempoMinutos() {
        return horaInicioMinutos + MARGEN_A_TIEMPO_MIN;
    }

    public int getFinTardanzaMinutos() {
        return horaInicioMinutos + MARGEN_TARDANZA_MIN;
    }

    /**
     * RN-AST-05: estado automático de marcación según los minutos transcurridos
     * desde medianoche al momento del check-in.
     */
    public EstadoAsistencia calcularEstadoPorMarcacion(int minutosDelDia) {
        if (minutosDelDia <= getFinATiempoMinutos()) {
            return EstadoAsistencia.A_TIEMPO;
        }
        if (minutosDelDia <= getFinTardanzaMinutos()) {
            return EstadoAsistencia.TARDANZA;
        }
        return EstadoAsistencia.FALTA_INJUSTIFICADA;
    }
}

package dominio;

/** Inconsciente (Nigeria): es lanzado al aire y no puede moverse, saltar ni patear durante 3 s. */
public class EfectoInconsciente extends EfectoTemporal {
    private static final long serialVersionUID = 1L;

    public EfectoInconsciente() { super("inconsciente", "Inconsciente", Reglas.DURACION_INCONSCIENTE); }

    @Override public boolean bloqueaAcciones() { return true; }

    @Override
    public void alAplicar(Partida partida, Jugador victima) { victima.lanzarAlAire(Reglas.IMPULSO_INCONSCIENTE); }
}

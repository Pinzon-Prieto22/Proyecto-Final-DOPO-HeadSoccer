package dominio;

/**
 * efecto poder nigeris
 */
public class EfectoInconsciente extends EfectoTemporal {
    public EfectoInconsciente() { 
        super("inconsciente", "Inconsciente", Reglas.DURACION_INCONSCIENTE); 
    }

    @Override public boolean bloqueaAcciones() { 
        return true; 
    }

    @Override
    public void alAplicar(Partida partida, Jugador victima) { 
        victima.lanzarAlAire(Reglas.IMPULSO_INCONSCIENTE); 
    }
}

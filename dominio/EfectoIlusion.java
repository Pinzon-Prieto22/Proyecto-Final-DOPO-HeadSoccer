package dominio;

/**
 * Confusión (Estados Unidos): el balón se divide en 8 balones idénticos (7 falsos) durante 2,5 s.
 * Al terminar el efecto los falsos desaparecen.
 */
public class EfectoIlusion extends EfectoTemporal {
    private static final long serialVersionUID = 1L;

    public EfectoIlusion() { super("ilusion", "Confundido", Reglas.DURACION_ILUSION); }

    @Override
    public void alAplicar(Partida partida, Jugador victima) { partida.crearBalonesFalsos(Reglas.BALONES_ILUSION - 1); }

    @Override
    public void alTerminar(Partida partida, Jugador victima) { partida.eliminarBalonesFalsos(); }
}

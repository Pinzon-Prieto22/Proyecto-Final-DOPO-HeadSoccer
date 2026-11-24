package dominio;

/**
 * efecto poder usa
 */
public class EfectoIlusion extends EfectoTemporal {
    public EfectoIlusion() { 
        super("ilusion", "Confundido", Reglas.DURACION_ILUSION); 
    }

    @Override
    public void alAplicar(Partida partida, Jugador victima) { 
        partida.crearBalonesFalsos(Reglas.BALONES_ILUSION - 1); 
    }

    @Override
    public void alTerminar(Partida partida, Jugador victima) { 
        partida.eliminarBalonesFalsos(); 
    }
}

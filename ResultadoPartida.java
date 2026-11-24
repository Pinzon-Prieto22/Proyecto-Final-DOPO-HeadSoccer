package dominio;

/**
 * Clase responsable del resultado o resumen de la partida
 */
public class ResultadoPartida {
    private final Lado ganador;
    private final Personaje[] personajes = new Personaje[2];
    private final String[] nombres = new String[2];
    private final int[] goles = new int[2];
    private final boolean muerteSubita;
    private final boolean terminadaPorUsuario;
    private final double duracion;

    public ResultadoPartida(Lado ganador, Jugador izq, Jugador der, int golesIzq, int golesDer,
                            boolean muerteSubita, boolean terminadaPorUsuario, double duracion) {
        this.ganador = ganador;
        Jugador[] js = { izq, der };
        for (int i = 0; i < 2; i++) {
            personajes[i] = js[i].getPersonaje();
            nombres[i] = js[i].getNombre();
        }
        goles[0] = golesIzq;
        goles[1] = golesDer;
        this.muerteSubita = muerteSubita;
        this.terminadaPorUsuario = terminadaPorUsuario;
        this.duracion = duracion;
    }

    public Lado getGanador(){ 
        return ganador; 
    }
    public Personaje getPersonaje(Lado lado){ 
        return personajes[lado.ordinal()]; 
    }
    public String getNombreJugador(Lado lado) { 
        return nombres[lado.ordinal()]; 
    }
    public int getGoles(Lado lado){ 
        return goles[lado.ordinal()]; 
    }
    public boolean huboMuerteSubita(){ 
        return muerteSubita; 
    }
    public boolean fueTerminadaPorUsuario(){ 
        return terminadaPorUsuario; 
    }
    public double getDuracion() { 
        return duracion; 
    }
}

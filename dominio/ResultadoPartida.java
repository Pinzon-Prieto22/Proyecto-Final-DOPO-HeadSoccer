package dominio;

/**
 * Clasd ResultadoPartida
 * Responsable del resumen del partido (terminado) 
 * incluye al gasador, marcado, los puntos obtenidos segun la citoria o derrota
 * 
 * @author Bryan Pinzon y Valentina Prieto
 * @version Entrega Final Proyecto Final
 */

public class ResultadoPartida {
    private final Lado ganador;
    private final Personaje[] personajes = new Personaje[2];
    private final String[] nombres = new String[2];
    private final boolean[] humanos = new boolean[2];
    private final int[] goles = new int[2];
    private final boolean muerteSubita;
    private final boolean terminadaPorUsuario;
    private final double duracion;

    /**
     * Constructor del Resultado de la partida
     * @param ganador
     * @param izq
     * @param der
     * @param golesIzq
     * @param golesDer
     * @param muerteSubita
     * @param terminadaPorUsuario
     * @param duracion
     */
    public ResultadoPartida(Lado ganador, Jugador izq, Jugador der, int golesIzq, int golesDer, boolean muerteSubita, boolean terminadaPorUsuario, double duracion) {
        this.ganador = ganador;
        // guardamos los jugadores
        Jugador[] js = { izq, der };
        // recorremos ,los jugadores y guardamos  sis datos
        for (int i = 0; i < 2; i++) {
            personajes[i] = js[i].getPersonaje();
            nombres[i] = js[i].getNombre();
            humanos[i] = js[i].esHumano();
        }
        goles[0] = golesIzq;
        goles[1] = golesDer;
        this.muerteSubita = muerteSubita;
        this.terminadaPorUsuario = terminadaPorUsuario;
        this.duracion = duracion;
    }

    //Metodos getter
    public Lado getGanador(){ 
        return ganador; 
    }
    public Personaje getPersonaje(Lado lado){ 
        return personajes[lado.ordinal()]; 
    }
    public String getNombreJugador(Lado lado){ 
        return nombres[lado.ordinal()]; 
    }
    public boolean esHumano(Lado lado){ 
        return humanos[lado.ordinal()]; 
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
    public double getDuracion(){ 
        return duracion; 
    }

    /**
     * Metodo que nos da los puntos del ganador y el perdedor segun el caso
     * @param lado
     * @return
     */
    public int getPuntosGanados(Lado lado){
        // las maquinas no ganan puntos*
        if (ganador == null || !esHumano(lado)) return 0;
        // segun el caso retornamos los puntos correspondientes
        return lado == ganador ? Reglas.PUNTOS_VICTORIA : Reglas.PUNTOS_DERROTA;
    }
}

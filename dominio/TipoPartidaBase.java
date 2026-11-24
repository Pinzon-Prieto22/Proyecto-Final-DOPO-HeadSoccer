package dominio;

/**
 * Clase abstracta TipoPartidaBase implementa TipoPartida
 * Responsable de los comportamientos y generalidadesd de una partida normal
 * 
 * es Serializable para poder guardarlo y abrirlo
 * 
 * @author Bryan Pinzon y Valentina Prieto
 * @version Entrega Final Proyecto Final
 */

public abstract class TipoPartidaBase implements TipoPartida {
    // indentificador de serializacion
    private static final long serialVersionUID = 1L;
    private boolean terminado;
    private Lado ganador;
    private boolean muerteSubita;

    /** 
     * Metodo que da inicio a la partida con sus valores
     * @see dominio.TipoPartida#iniciar(dominio.Partida)
     */
    @Override 
    public void iniciar(Partida partida) { 
        terminado = false; 
        ganador = null; 
        muerteSubita = false; 
    }

    /**
     * Metodo de como pasa el tiempo (cada partida lo define)
     * @see dominio.TipoPartida#alPasarElTiempo(dominio.Partida, double)
     */
    @Override 
    public void alPasarElTiempo(Partida partida, double dt) { }

    /** 
     * Metodo que dice si ya termino
     * @see dominio.TipoPartida#haTerminado()
     */
    @Override 
    public boolean haTerminado() { 
        return terminado; 
    }
    /**
     * Metodo que nos da el ganador
     * @see dominio.TipoPartida#getGanador()
     */
    @Override 
    public Lado getGanador() { 
        return ganador; 
    }
    /**
     * Metodo que nos da el tiempo restante
     * @see dominio.TipoPartida#getTiempoRestante()
     */
    @Override 
    public double getTiempoRestante() { 
        return -1; 
    }
    /**
     * MEtodo que nos dice si se encuenta en muertesubita
     * @see dominio.TipoPartida#enMuerteSubita()
     */
    @Override 
    public boolean enMuerteSubita() { 
        return muerteSubita; 
    }
    /**
     * Metodo que finalisza la partida
     * @param ganador
     */
    protected void terminar(Lado ganador) { 
        this.terminado = true; 
        this.ganador = ganador; 
    }
    /**
     * Metodo que activa la muerte subita
     */
    protected void activarMuerteSubita() { 
        this.muerteSubita = true; 
    }
}

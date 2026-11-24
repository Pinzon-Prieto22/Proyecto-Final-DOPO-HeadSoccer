package dominio;

/**
 * Clase PArtidaPorTiempo
 * Responsable de las reglas y funcionamiento del tipo de partida que es por tiempo
 * Gana quien obtenbga mas goles en el tiempo establecido, en caso de empate hay muerte subita
 * 
 * es Serializable para poder guardarlo y abrirlo
 * 
 * @author Bryan Pinzon y Valentina Prieto
 * @version Entrega Final Proyecto Final
 */

public class PartidaPorTiempo extends TipoPartidaBase {
    // indentificador de serializacion
    private static final long serialVersionUID = 1L;
    private final int minutos;
    private double restante;

    /**
     * Constructor de la partidaportiempo
     * @param minutos
     */
    public PartidaPorTiempo(int minutos) {
        this.minutos = minutos;
        this.restante = minutos * 60.0;
    }

    /** 
     * Sobreescribimos el metodo del nombre del tipo de la partida
     * @see dominio.TipoPartida#getNombre()
     */
    @Override public String getNombre(){ 
        return "Por tiempo (" + minutos + " min)"; 
    }

    /**
     * Sobreescribimos el inicial la partida
     * @see dominio.TipoPartidaBase#iniciar(dominio.Partida)
     */
    @Override
    public void iniciar(Partida partida){ 
        super.iniciar(partida); restante = minutos * 60.0; 
    }

    /**
     * Sobreescribimos como pasa el tiempo en la partida
     * @see dominio.TipoPartidaBase#alPasarElTiempo(dominio.Partida, double)
     */
    @Override
    public void alPasarElTiempo(Partida partida, double dt) {
        if (haTerminado() || enMuerteSubita()) return;
        // vamos disminuyendo el tiempo
        restante -= dt;
        if (restante > 0) return;
        restante = 0;
        int i = partida.getMarcador().getGoles(Lado.IZQUIERDO);
        int d = partida.getMarcador().getGoles(Lado.DERECHO);
        if (i != d) terminar(i > d ? Lado.IZQUIERDO : Lado.DERECHO);
        else activarMuerteSubita();
    }

    /**
     * Sobreescribimos lo que pasa al anotar un gol
     * @see dominio.TipoPartida#alAnotarse(dominio.Partida, dominio.Lado)
     */
    @Override
    public void alAnotarse(Partida partida, Lado anotador) {
        if (enMuerteSubita()) terminar(anotador);
    }

    /**    (non-Javadoc)
     * Sobreescribimos el metodo que nos dice el tiempo restante
     * @see dominio.TipoPartidaBase#getTiempoRestante()
     */
    @Override public double getTiempoRestante() { 
        return restante; 
    }
}

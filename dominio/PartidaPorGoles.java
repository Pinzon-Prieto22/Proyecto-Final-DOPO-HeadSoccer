package dominio;

/**
 * Clase PartidaPorGoles
 * Responsable de las reglas y funcionamiento del tipo de partida que es por goles
 * Gana quien anote de primeras los goles establecidos
 * 
 * es Serializable para poder guardarlo y abrirlo
 * 
 * @author Bryan Pinzon y Valentina Prieto
 * @version Entrega Final Proyecto Final
 */

public class PartidaPorGoles extends TipoPartidaBase {
    //identificador de serializacion
    private static final long serialVersionUID = 1L;
    private final int meta;
    /**
     * Constructor de la partida por goles
     * @param meta
     */
    public PartidaPorGoles(int meta) { 
        this.meta = meta; 
    }

    /**
     * Sobreescribimos el metodo que nos da el nombre del tiepo
     * @see dominio.TipoPartida#getNombre()
     */
    @Override 
    public String getNombre() { 
        return "Por goles (a " + meta + ")"; 
    }

    /**
     * Sobreescribimos el metodo de lo que pasa cuando se anota el gol
     * @see dominio.TipoPartida#alAnotarse(dominio.Partida, dominio.Lado)
     */
    @Override
    public void alAnotarse(Partida partida, Lado anotador) {
        if (partida.getMarcador().getGoles(anotador) >= meta) terminar(anotador);
    }
}

package dominio;

/**
 * Interfaz FabricaTipoPartida
 * Define la creacion de un tipo de partida
 * 
 * @author Bryan Pinzon y Valentina Prieto
 * @version Entrega Final Proyecto Final
 */

public interface FabricaTipoPartida {
    String getId();
    String getNombre();
    String getDescripcion();
    String getUnidad(); //unidad de medida de la partida (tiempo o goles)
    int getMinimo();
    int getMaximo();
    int getValorPorDefecto();
    TipoPartida crear(int valor) throws DOPOSoccerException;
}

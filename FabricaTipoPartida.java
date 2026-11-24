package dominio;

public interface FabricaTipoPartida {
    String getId();
    String getNombre();
    String getDescripcion();
    
    String getUnidad();
    int getMinimo();
    int getMaximo();
    int getValorPorDefecto();
    TipoPartida crear(int valor) throws DOPOSoccerException;
}

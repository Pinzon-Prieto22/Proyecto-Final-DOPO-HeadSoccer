package dominio;

/**
 * Clase FabricaPorTiempo que implementa la fabricaTipoPartida
 * Se encarga de la creacion de la partida por tiempo
 * 
 * @author Bryan Pinzon y Valentina Prieto
 * @version Entrega Final Proyecto Final
 */

public class FabricaPorGoles implements FabricaTipoPartida {
    @Override public String getId(){ 
        return "goles"; 
    }
    @Override public String getNombre(){ 
        return "Por goles"; 
    }
    @Override public String getDescripcion(){ 
        return "Sin limite de tiempo: gana el primero que llegue al numero de goles que elijas."; 
    }
    // unidad en la que se mide/rige el partido
    @Override public String getUnidad(){ 
        return "goles"; 
    }
    // valores min y max y por defecto
    @Override public int getMinimo(){ 
        return 1; 
    }
    @Override public int getMaximo(){ 
        return 30; 
    }
    @Override public int getValorPorDefecto(){ 
        return 5; 
    }

    /**
     * Metodo que se encarga de la creacion de la partida
     * @see dominio.FabricaTipoPartida#crear(int)
     */
    @Override
    public TipoPartida crear(int goles) throws DOPOSoccerException{
        if (goles < getMinimo() || goles > getMaximo()) {
            throw new DOPOSoccerException("El numero de goles para ganar debe estar entre " + getMinimo() + " y " + getMaximo() + " (recibido: " + goles + ").",
                    "Elige otro numero de goles.");
        }
        return new PartidaPorGoles(goles);
    }
}

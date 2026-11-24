package dominio;

/**
 * Clase FabricaPorTiempo que implementa la fabricaTipoPartida
 * Se encarga de la creacion de la partida por tiempo
 * 
 * @author Bryan Pinzon y Valentina Prieto
 * @version Entrega Final Proyecto Final
 */

public class FabricaPorTiempo implements FabricaTipoPartida {
    @Override public String getId(){ 
        return "tiempo"; 
    }
    @Override public String getNombre(){ 
        return "Por tiempo"; 
    }
    @Override public String getDescripcion(){
        return "El partido dura los minutos que elijas. Gana quien tenga mas goles al final; si hay empate, muerte subita: gana el primero que marque.";
    }
    // unidad en la que se mide/rige el partido
    @Override public String getUnidad(){ 
        return "minutos"; 
    }
    // valores min y max y por defecto
    @Override public int getMinimo(){ 
        return 1; 
    }
    @Override public int getMaximo(){ 
        return 30; 
    }
    @Override public int getValorPorDefecto(){ 
        return 2; 
    }

    /**
     * Metodo que se encarga de crear la parida por minutos
     * @see dominio.FabricaTipoPartida#crear(int)
     */
    @Override
    public TipoPartida crear(int minutos) throws DOPOSoccerException{
        if (minutos < getMinimo() || minutos > getMaximo()) {
            throw new DOPOSoccerException("La duracion del partido debe estar entre " + getMinimo() + " y " + getMaximo() + " minutos (recibido: " + minutos + ").",
                    "Elige otro numero de minutos.");
        }
        return new PartidaPorTiempo(minutos);
    }
}

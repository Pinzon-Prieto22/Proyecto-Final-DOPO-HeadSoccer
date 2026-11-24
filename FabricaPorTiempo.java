package dominio;

public class FabricaPorTiempo implements FabricaTipoPartida {
    @Override public String getId() { 
        return "tiempo"; 
    }
    @Override public String getNombre() { 
        return "Por tiempo"; 
    }
    @Override public String getDescripcion() {
        return "El partido dura los minutos que elijas. Gana quien tenga más goles al final; si hay empate, muerte súbita: gana el primero que marque.";
    }
    @Override public String getUnidad() { 
        return "minutos"; 
    }
    @Override public int getMinimo() { 
        return 1; 
    }
    @Override public int getMaximo() { 
        return 30;
    }
    @Override public int getValorPorDefecto() { 
        return 2; 
    }

    @Override
    public TipoPartida crear(int minutos) throws DOPOSoccerException {
        if (minutos < getMinimo() || minutos > getMaximo()) {
            throw new DOPOSoccerException("La duración del partido debe estar entre " + getMinimo() + " y " + getMaximo() + " minutos (recibido: " + minutos + ").",
                    "Elige otro número de minutos.");
        }
        return new PartidaPorTiempo(minutos);
    }
}

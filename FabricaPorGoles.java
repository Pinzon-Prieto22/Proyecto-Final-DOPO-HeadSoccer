package dominio;

public class FabricaPorGoles implements FabricaTipoPartida {
    @Override 
    public String getId() { 
        return "goles"; 
    }
    @Override 
    public String getNombre() { 
        return "Por goles"; 
    }
    @Override 
    public String getDescripcion() { 
        return "Sin límite de tiempo: gana el primero que llegue al número de goles que elijas."; 
    }
    @Override 
    public String getUnidad() { 
        return "goles"; 
    }
    @Override 
    public int getMinimo() { 
        return 1; 
    }
    @Override 
    public int getMaximo() { 
        return 30; 
    }
    @Override 
    public int getValorPorDefecto() { 
        return 5; 
    }

    @Override
    public TipoPartida crear(int goles) throws DOPOSoccerException {
        if (goles < getMinimo() || goles > getMaximo()) {
            throw new DOPOSoccerException("El número de goles para ganar debe estar entre " + getMinimo() + " y " + getMaximo() + " (recibido: " + goles + ").",
                    "Elige otro número de goles.");
        }
        return new PartidaPorGoles(goles);
    }
}

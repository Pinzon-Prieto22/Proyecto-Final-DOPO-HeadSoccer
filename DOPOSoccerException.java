package dominio;

/**
 * Clase responsable de las excepciones del juego
 */
public class DOPOSoccerException extends Exception {
    private static final long serialVersionUID = 1L;
    
    private final String sugerencia;

    public DOPOSoccerException(String mensaje){ 
        this(mensaje, null, null); 
    }
    public DOPOSoccerException(String mensaje, String sugerencia){
        this(mensaje, sugerencia, null);
    }

    public DOPOSoccerException(String mensaje, String sugerencia, Throwable causa){
        super(mensaje, causa);
        this.sugerencia = sugerencia;
    }

    public String getSugerencia(){ 
        return sugerencia; 
    }
}

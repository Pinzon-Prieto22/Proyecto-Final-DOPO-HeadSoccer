package dominio;

/**
 * Clase DOPOSoccerException
 * Responsable de las excepciones de nuestro juego
 * 
 * @author Bryan Pinzon y Valentina Prieto
 * @version Entrega Final Proyecto Final
 */

public class DOPOSoccerException extends Exception {
    // identificador de seiralizacion
    private static final long serialVersionUID = 1L;

    private final String sugerencia;

    /**
     * Constructor con solo mensaje
     * @param mensaje
     */
    public DOPOSoccerException(String mensaje) { 
        this(mensaje, null, null); 
    }

    /**
     * Constructor con mensage y sugerencia
     * @param mensaje
     * @param sugerencia
     */
    public DOPOSoccerException(String mensaje, String sugerencia) { 
        this(mensaje, sugerencia, null); 
    }

    /**
     * Constructor con mensaje, sugerencia y causa
     * @param mensaje
     * @param sugerencia
     * @param causa
     */
    public DOPOSoccerException(String mensaje, String sugerencia, Throwable causa) {
        super(mensaje, causa);
        this.sugerencia = sugerencia;
    }

    /**
     * Metodo que nos da lo que podria hacer el usuario
     * @return
     */
    public String getSugerencia() { 
        return sugerencia; 
    }
}

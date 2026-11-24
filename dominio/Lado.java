package dominio;

/**
 * Enum Lado
 * Responsable de guardar los lados de la cancha del juego
 * 
 * @author Bryan Pinzon y Valentina Prieto
 * @version Entrega Final Proyecto Final
 */

public enum Lado {

    IZQUIERDO, DERECHO;

    /**
     * Metodo que nos da el lado opuesto
     * @return el Lado opuesto
     */
    public Lado opuesto() {
        return this == IZQUIERDO ? DERECHO : IZQUIERDO;
    }
}

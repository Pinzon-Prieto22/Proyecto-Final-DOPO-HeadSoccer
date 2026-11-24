package dominio;
/**
 * Enum donde guardamos los lados de la cancha
 * 
 */
public enum Lado {
    IZQUIERDO, DERECHO;

    public Lado opuesto() {
        return this == IZQUIERDO ? DERECHO : IZQUIERDO;
    }
}

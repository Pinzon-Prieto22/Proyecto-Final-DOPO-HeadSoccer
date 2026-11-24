package dominio;

import java.io.Serializable;
import java.util.Set;

/**
 * Decide qué acciones realiza un jugador en cada paso: ControladorHumano (teclado) o ControladorIA (máquina).
 * La Partida no sabe cuál es cuál, así que se pueden agregar máquinas nuevas sin tocarla.
 */
public interface Controlador extends Serializable {
    /** Acciones "presionadas" en este instante. El conjunto devuelto NO debe modificarse. */
    Set<Accion> decidir(Jugador yo, Partida partida, double dt);
}

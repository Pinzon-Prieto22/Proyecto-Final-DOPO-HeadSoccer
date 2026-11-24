package dominio;

import java.util.Set;

/**
 * Interfaz para el controlador
 */
public interface Controlador {
    Set<Accion> decidir(Jugador yo, Partida partida, double dt);
}
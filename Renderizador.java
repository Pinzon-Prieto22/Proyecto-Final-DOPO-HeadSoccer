package presentacion;

import java.awt.Graphics2D;
import dominio.Partida;

/**
 * Interfaz para dibujar y todo lo relacionado a lo visual
 */
public interface Renderizador {
    void dibujar(Graphics2D g, Partida partida);
}

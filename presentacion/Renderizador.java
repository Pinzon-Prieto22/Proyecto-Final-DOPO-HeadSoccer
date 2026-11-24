package presentacion;

import java.awt.Graphics2D;
import dominio.Partida;

/**
 * Dibuja una capa de la cancha en coordenadas del mundo (960 x 540). PanelCancha mantiene una lista
 * ordenada de renderizadores: agregar un elemento visual nuevo es crear una clase y añadirla a la lista.
 */
public interface Renderizador {
    void dibujar(Graphics2D g, Partida partida);
}

package presentacion;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import dominio.Partida;
import dominio.Reglas;
import javax.swing.JPanel;

/**
 * Lienzo donde se dibuja el partido
 */
public class PanelCancha extends JPanel {
    private final Partida partida;
    private final Renderizador[] capas = {
        new RenderizadorFondo(), new RenderizadorArco(), new RenderizadorJugador(),
        new RenderizadorBalon(), new RenderizadorMensajes()
    };

    public PanelCancha(Partida partida) {
        this.partida = partida;
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension((int) Reglas.ANCHO, (int) Reglas.ALTO));
        setFocusable(false);
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0.create();
        Estilos.activarCalidad(g);
        double escala = Math.min(getWidth() / Reglas.ANCHO, getHeight() / Reglas.ALTO);
        double ox = (getWidth() - Reglas.ANCHO * escala) / 2, oy = (getHeight() - Reglas.ALTO * escala) / 2;
        g.translate(ox, oy);
        g.scale(escala, escala);
        g.clipRect(0, 0, (int) Reglas.ANCHO, (int) Reglas.ALTO);
        for (Renderizador r : capas) r.dibujar(g, partida);
        g.dispose();
    }
}

package presentacion;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import dominio.Arco;
import dominio.Lado;
import dominio.Partida;
import dominio.Reglas;
/**
 * Clase que dibuja los arcos
 */
public class RenderizadorArco implements Renderizador {
    @Override
    public void dibujar(Graphics2D g, Partida p) {
        for (Lado l : Lado.values()) dibujarArco(g, p.getCancha().getArco(l));
    }

    private void dibujarArco(Graphics2D g, Arco a) {
        double x = a.getX(), w = a.getAncho(), top = a.getYTravesano(), suelo = Reglas.Y_SUELO;
        g.setColor(new Color(255, 255, 255, 35));
        g.fill(new Rectangle2D.Double(x, top, w, suelo - top));
        g.setColor(new Color(255, 255, 255, 120));
        g.setStroke(new BasicStroke(1f));
        for (double xx = x; xx <= x + w; xx += 10) g.draw(new Line2D.Double(xx, top, xx, suelo));
        for (double yy = top; yy <= suelo; yy += 10) g.draw(new Line2D.Double(x, yy, x + w, yy));
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke((float) Reglas.GROSOR_TRAVESANO));
        g.draw(new Line2D.Double(x, top + Reglas.GROSOR_TRAVESANO / 2, x + w, top + Reglas.GROSOR_TRAVESANO / 2));
        double poste = a.getLado() == Lado.IZQUIERDO ? x + w : x;
        g.setStroke(new BasicStroke(8f));
        g.draw(new Line2D.Double(poste, top, poste, suelo));
    }
}

package presentacion;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;
import dominio.Personaje;

/** Dibuja un personaje con figuras de Java2D. Lo comparten la cancha y la pantalla de selección. */
public final class DibujoPersonaje {
    private DibujoPersonaje() { }

    /**
     * @param x,y       punto de los pies (centro, en el suelo)
     * @param e         escala (1 = tamaño normal)
     * @param dir       +1 mira a la derecha, -1 a la izquierda
     * @param pieX,pieY posición del pie que patea
     */
    public static void dibujar(Graphics2D g, Personaje p, double x, double y, double e, int dir, double pieX, double pieY) {
        AspectoPersonaje a = AspectoPersonaje.de(p);
        Color contorno = new Color(40, 25, 15);

        g.setStroke(new BasicStroke((float) (10 * e), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(a.piel);
        g.draw(new Line2D.Double(x - dir * 5 * e, y - 28 * e, x - dir * 9 * e, y - 7 * e));   // pierna de apoyo
        g.draw(new Line2D.Double(x + dir * 3 * e, y - 28 * e, pieX, pieY));                    // pierna que patea
        g.setColor(a.zapato);
        g.fill(new Ellipse2D.Double(x - dir * 9 * e - 12 * e, y - 12 * e, 24 * e, 12 * e));    // zapato de apoyo

        g.setStroke(new BasicStroke((float) Math.max(1.5, 2.2 * e)));
        RoundRectangle2D tronco = new RoundRectangle2D.Double(x - 17 * e, y - 46 * e, 34 * e, 34 * e, 12 * e, 12 * e);
        g.setColor(a.camisa);
        g.fill(tronco);
        g.setColor(a.pantaloneta);
        g.fill(new RoundRectangle2D.Double(x - 17 * e, y - 22 * e, 34 * e, 13 * e, 6 * e, 6 * e));
        g.setColor(contorno);
        g.draw(tronco);

        double r = 38 * e, hx = x, hy = y - 70 * e;
        Ellipse2D cabeza = new Ellipse2D.Double(hx - r, hy - r, 2 * r, 2 * r);
        g.setColor(a.piel);
        g.fill(cabeza);
        g.setColor(a.cabello);
        g.fill(new Arc2D.Double(hx - r, hy - r, 2 * r, 2 * r, 15, 150, Arc2D.CHORD));
        g.setColor(contorno);
        g.draw(cabeza);

        for (int i = 0; i < 2; i++) {                    // ojos (miran hacia donde mira el personaje)
            double ex = hx + dir * r * (0.22 + 0.42 * i);
            double ey = hy - r * 0.02;
            g.setColor(Color.WHITE);
            g.fill(new Ellipse2D.Double(ex - r * 0.15, ey - r * 0.2, r * 0.3, r * 0.4));
            g.setColor(contorno);
            g.fill(new Ellipse2D.Double(ex - r * 0.06 + dir * r * 0.05, ey - r * 0.08, r * 0.14, r * 0.2));
            g.draw(new Line2D.Double(ex - r * 0.2, ey - r * (0.3 - 0.1 * dir * (i == 0 ? 1 : -1)), ex + r * 0.2, ey - r * 0.28));
        }
        g.setColor(contorno);
        g.draw(new Arc2D.Double(hx + dir * r * 0.18 - r * 0.2, hy + r * 0.25, r * 0.5, r * 0.3, 200, 140, Arc2D.OPEN));

        g.setColor(a.zapato);                              // zapato del pie que patea
        g.fill(new Ellipse2D.Double(pieX - 15 * e + dir * 5 * e, pieY - 6 * e, 30 * e, 16 * e));
        g.setColor(Color.WHITE);
        g.fill(new Ellipse2D.Double(pieX - 13 * e + dir * 5 * e, pieY + 5 * e, 26 * e, 4 * e));
    }
}

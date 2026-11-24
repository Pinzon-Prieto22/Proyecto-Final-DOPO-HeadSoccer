package presentacion;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.LinkedList;
import dominio.Balon;
import dominio.Partida;
import dominio.Reglas;

/**
 * Dibuja el balón real y, durante la Ilusión, los balones falsos: todos se dibujan con el MISMO método, así los
 * 8 se ven idénticos para ambos jugadores. Cuando el balón lleva un tiro de poder muestra el efecto de ese tiro.
 */
public class RenderizadorBalon implements Renderizador {
    private final LinkedList<double[]> estela = new LinkedList<double[]>();

    @Override
    public void dibujar(Graphics2D g, Partida p) {
        for (Balon f : p.getBalonesFalsos()) dibujarBalon(g, f);
        Balon b = p.getBalon();
        if (b.tienePoder()) decorarTiro(g, b);
        else estela.clear();
        dibujarBalon(g, b);
    }

    private void dibujarBalon(Graphics2D g, Balon b) {
        double x = b.getX(), y = b.getY(), r = b.getRadio();
        double altura = Math.max(0, Reglas.Y_SUELO - (y + r));
        double s = Math.max(0.3, 1 - altura / 500);
        g.setColor(new Color(0, 0, 0, 75));
        g.fill(new Ellipse2D.Double(x - r * s, Reglas.Y_SUELO - 4, 2 * r * s, 8));

        Graphics2D g2 = (Graphics2D) g.create();
        g2.translate(x, y);
        g2.rotate(b.getAngulo());
        g2.setColor(Color.WHITE);
        g2.fill(new Ellipse2D.Double(-r, -r, 2 * r, 2 * r));
        g2.setColor(new Color(30, 30, 30));
        Polygon pent = new Polygon();
        for (int i = 0; i < 5; i++) {
            double a = Math.toRadians(-90 + i * 72);
            pent.addPoint((int) Math.round(Math.cos(a) * r * 0.42), (int) Math.round(Math.sin(a) * r * 0.42));
            g2.setStroke(new BasicStroke(1.6f));
            g2.draw(new Line2D.Double(Math.cos(a) * r * 0.42, Math.sin(a) * r * 0.42, Math.cos(a) * r * 0.85, Math.sin(a) * r * 0.85));
            double a2 = a + Math.toRadians(36);
            g2.fill(new Ellipse2D.Double(Math.cos(a2) * r * 0.82 - r * 0.2, Math.sin(a2) * r * 0.82 - r * 0.2, r * 0.4, r * 0.4));
        }
        g2.fillPolygon(pent);
        g2.setStroke(new BasicStroke(2f));
        g2.draw(new Ellipse2D.Double(-r, -r, 2 * r, 2 * r));
        g2.dispose();
    }

    /** Estela y resplandor del tiro; cada poder tiene su propio aspecto. */
    private void decorarTiro(Graphics2D g, Balon b) {
        String id = b.getPoder().getId();
        Color c = AspectoPoder.color(id);
        double x = b.getX(), y = b.getY(), r = b.getRadio();
        long t = System.currentTimeMillis();

        estela.addFirst(new double[] { x, y });
        while (estela.size() > 14) estela.removeLast();
        int i = 0;
        for (double[] pt : estela) {
            double rr = r * (1.4 - i * 0.05);
            g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), Math.max(0, 150 - i * 11)));
            g.fill(new Ellipse2D.Double(pt[0] - rr, pt[1] - rr, 2 * rr, 2 * rr));
            i++;
        }
        g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 90));
        g.fill(new Ellipse2D.Double(x - r * 2.2, y - r * 2.2, r * 4.4, r * 4.4));

        if (id.equals("rayo")) {                              // electricidad
            g.setStroke(new BasicStroke(2.5f));
            g.setColor(new Color(255, 250, 120));
            for (int k = 0; k < 4; k++) {
                Path2D arco = new Path2D.Double();
                double ang = (t / 80 + k * 7) % 12 * Math.PI / 6;
                arco.moveTo(x + Math.cos(ang) * r, y + Math.sin(ang) * r);
                for (int s = 1; s <= 3; s++) {
                    double jit = ((t / 50 + k + s) % 3 - 1) * 5;
                    arco.lineTo(x + Math.cos(ang + jit * 0.05) * (r + s * 9) + jit, y + Math.sin(ang + jit * 0.05) * (r + s * 9));
                }
                g.draw(arco);
            }
        } else if (id.equals("tornado")) {                    // remolino
            AffineTransform viejo = g.getTransform();
            g.translate(x, y);
            g.setStroke(new BasicStroke(2.5f));
            for (int k = 0; k < 4; k++) {
                g.setColor(new Color(220, 230, 240, 190 - k * 30));
                g.rotate(t / 120.0 + k);
                double ancho = 70 - k * 12;
                g.draw(new Ellipse2D.Double(-ancho / 2, -r * 0.5 - k * 3, ancho, r + k * 6));
            }
            g.setTransform(viejo);
        } else if (id.equals("ilusion")) {                    // destellos
            g.setColor(new Color(255, 255, 255, 220));
            for (int k = 0; k < 6; k++) {
                double ang = t / 200.0 + k * Math.PI / 3, rr = r * 1.8 + 4 * Math.sin(t / 90.0 + k);
                g.fill(new Ellipse2D.Double(x + Math.cos(ang) * rr - 2.5, y + Math.sin(ang) * rr - 2.5, 5, 5));
            }
        }
    }
}

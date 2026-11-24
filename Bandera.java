package presentacion;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Arc2D;

/**
 * Dibuja las banderas
 */
public final class Bandera {
    private Bandera() { }

    public static void dibujar(Graphics2D g0, String codigo, int x, int y, int w, int h) {
        Graphics2D g = (Graphics2D) g0.create(x, y, w, h);
        Estilos.activarCalidad(g);
        dibujarSencilla(g, codigo.toUpperCase(), w, h);
        g.dispose();
        g0.setColor(new Color(0, 0, 0, 170));
        g0.drawRect(x, y, w - 1, h - 1);
    }

    private static void dibujarSencilla(Graphics2D g, String c, int w, int h) {
        Color rojo = new Color(205, 30, 45), verde = new Color(0, 130, 60), amarillo = new Color(252, 209, 22);
        Color azul = new Color(10, 40, 130), blanco = Color.WHITE;
        if (c.equals("KOR")) {
            g.setColor(blanco); g.fillRect(0, 0, w, h);
            int r = h / 3;
            g.setColor(rojo); g.fill(new Arc2D.Double(w / 2.0 - r, h / 2.0 - r, 2 * r, 2 * r, 0, 180, Arc2D.PIE));
            g.setColor(new Color(0, 70, 160)); g.fill(new Arc2D.Double(w / 2.0 - r, h / 2.0 - r, 2 * r, 2 * r, 180, 180, Arc2D.PIE));
            g.setColor(Color.BLACK);
            g.fillRect(w / 12, h / 8, w / 8, 2); g.fillRect(w / 12, h / 4, w / 8, 2);
            g.fillRect(w * 10 / 12 - w / 24, h * 6 / 8, w / 8, 2); g.fillRect(w * 10 / 12 - w / 24, h * 5 / 8, w / 8, 2);
        } else if (c.equals("CMR")) {
            g.setColor(verde); g.fillRect(0, 0, w / 3, h);
            g.setColor(rojo); g.fillRect(w / 3, 0, w / 3 + 1, h);
            g.setColor(amarillo); g.fillRect(w * 2 / 3, 0, w - w * 2 / 3, h);
            estrella(g, w / 2.0, h / 2.0, h * 0.26, amarillo);
        } else if (c.equals("NGA")) {
            g.setColor(verde); g.fillRect(0, 0, w / 3, h);
            g.setColor(blanco); g.fillRect(w / 3, 0, w / 3 + 1, h);
            g.setColor(verde); g.fillRect(w * 2 / 3, 0, w - w * 2 / 3, h);
        } else if (c.equals("USA")) {
            for (int i = 0; i < 7; i++) {
                g.setColor(i % 2 == 0 ? new Color(190, 30, 50) : blanco);
                g.fillRect(0, i * h / 7, w, h / 7 + 1);
            }
            g.setColor(azul); g.fillRect(0, 0, w * 45 / 100, h * 4 / 7);
            g.setColor(blanco);
            for (int i = 0; i < 3; i++) for (int j = 0; j < 4; j++) g.fillRect(w / 14 + j * w / 11, h / 12 + i * h / 6, 2, 2);
        } else {
            g.setColor(new Color(160, 160, 170)); g.fillRect(0, 0, w, h);
            g.setColor(Color.WHITE);
            g.setFont(Estilos.fuente(Math.max(9, h / 2)));
            g.drawString(c, 3, h * 2 / 3);
        }
    }

    private static void estrella(Graphics2D g, double cx, double cy, double r, Color color) {
        Polygon p = new Polygon();
        for (int i = 0; i < 10; i++) {
            double rr = i % 2 == 0 ? r : r * 0.42, a = Math.toRadians(-90 + i * 36);
            p.addPoint((int) Math.round(cx + Math.cos(a) * rr), (int) Math.round(cy + Math.sin(a) * rr));
        }
        g.setColor(color);
        g.fillPolygon(p);
    }
}

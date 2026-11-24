package presentacion;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import dominio.Personaje;
import dominio.Reglas;

/** Vista previa grande de un personaje (la misma que se dibuja en la cancha). */
public class VistaPersonaje extends javax.swing.JComponent {
    private final int dir;
    private Personaje personaje;

    public VistaPersonaje(int dir) {
        this.dir = dir;
        setPreferredSize(new Dimension(230, 240));
    }

    public void setPersonaje(Personaje p) { this.personaje = p; repaint(); }

    @Override
    protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0.create();
        Estilos.activarCalidad(g);
        int w = getWidth(), h = getHeight();
        g.setPaint(new GradientPaint(0, 0, new Color(120, 190, 90), 0, h, new Color(60, 130, 60)));
        g.fillRoundRect(2, 2, w - 5, h - 5, 36, 36);
        g.setColor(new Color(240, 215, 120));
        g.setStroke(new java.awt.BasicStroke(5f));
        g.drawRoundRect(2, 2, w - 5, h - 5, 36, 36);
        if (personaje != null) {
            double e = 1.7, y = h - 34, x = w / 2.0;
            g.setColor(new Color(0, 0, 0, 70));
            g.fill(new Ellipse2D.Double(x - 55, y - 6, 110, 16));
            DibujoPersonaje.dibujar(g, personaje, x, y, e, dir, x + dir * 14 * e, y - 9 * e);
        }
        g.dispose();
    }
}

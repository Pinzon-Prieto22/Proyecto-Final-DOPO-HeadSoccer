package presentacion;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JComponent;

/** Barra de vida (JComponent): verde con mucha vida, roja cuando queda poca. Muestra "ATURDIDO" si la vida llegó a cero. */
public class BarraVida extends JComponent {
    private final boolean invertida;
    private double vida = 100, maxima = 100;
    private boolean aturdido;

    public BarraVida(boolean invertida) {
        this.invertida = invertida;
        setPreferredSize(new Dimension(260, 22));
    }

    public void setValores(double vida, double maxima, boolean aturdido) {
        this.vida = vida;
        this.maxima = maxima;
        this.aturdido = aturdido;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0.create();
        Estilos.activarCalidad(g);
        int w = getWidth(), h = getHeight();
        RoundRectangle2D fondo = new RoundRectangle2D.Double(1, 1, w - 3, h - 3, h, h);
        g.setColor(new Color(10, 15, 35));
        g.fill(fondo);
        double f = maxima <= 0 ? 0 : Math.max(0, Math.min(1, vida / maxima));
        double ancho = (w - 8) * f;
        if (ancho > 0) {
            Color a = f > 0.5 ? new Color(90, 220, 90) : f > 0.25 ? new Color(250, 210, 60) : new Color(240, 70, 60);
            g.setPaint(new GradientPaint(0, 0, a.brighter(), 0, h, a.darker()));
            double x = invertida ? w - 4 - ancho : 4;
            g.fill(new RoundRectangle2D.Double(x, 4, ancho, h - 8, h - 8, h - 8));
        }
        g.setColor(Color.WHITE);
        g.setStroke(new java.awt.BasicStroke(2f));
        g.draw(fondo);
        String t = aturdido ? "\u00a1ATURDIDO!" : "VIDA " + (int) Math.ceil(vida) + " / " + (int) Math.round(maxima);
        g.setFont(Estilos.fuente(13));
        FontMetrics fm = g.getFontMetrics();
        int tx = (w - fm.stringWidth(t)) / 2, ty = (h + fm.getAscent() - fm.getDescent()) / 2;
        g.setColor(Color.BLACK); g.drawString(t, tx + 1, ty + 1);
        g.setColor(Color.WHITE); g.drawString(t, tx, ty);
        g.dispose();
    }
}

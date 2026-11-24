package presentacion;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JComponent;

/** Barra de poder (JComponent con pintado propio). Parpadea cuando está llena o el poder está aura. */
public class BarraPoder extends JComponent {
    private final boolean invertida;
    private final String tecla;
    private double porcentaje;
    private boolean aura;
    private Color colorPoder = Color.WHITE;

    /** @param invertida true para el jugador derecho: la barra se llena de derecha a izquierda */
    public BarraPoder(boolean invertida, String tecla) {
        this.invertida = invertida;
        this.tecla = tecla;
        setPreferredSize(new Dimension(260, 28));
    }

    public void setEstado(double porcentaje, boolean aura, Color colorPoder) {
        this.porcentaje = porcentaje;
        this.aura = aura;
        this.colorPoder = colorPoder;
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

        boolean lleno = porcentaje >= 100;
        double ancho = (w - 8) * Math.min(100, porcentaje) / 100.0;
        if (ancho > 0) {
            Color a = aura ? colorPoder : lleno ? new Color(255, 200, 40) : new Color(70, 200, 255);
            Color b = aura ? colorPoder.darker() : lleno ? new Color(255, 120, 0) : new Color(20, 110, 220);
            g.setPaint(new GradientPaint(0, 0, a, 0, h, b));
            double x = invertida ? w - 4 - ancho : 4;
            g.fill(new RoundRectangle2D.Double(x, 4, ancho, h - 8, h - 8, h - 8));
        }
        boolean brilla = (lleno || aura) && (System.currentTimeMillis() / 250) % 2 == 0;
        g.setColor(brilla ? Color.YELLOW : Color.WHITE);
        g.setStroke(new java.awt.BasicStroke(brilla ? 3f : 2f));
        g.draw(fondo);

        String texto = aura ? "\u00a1AURA ACTIVA! Toca el bal\u00f3n" : lleno ? "\u00a1LISTO! [" + tecla + "]" : "PODER " + (int) porcentaje + "%";
        g.setFont(Estilos.fuente(13));
        FontMetrics fm = g.getFontMetrics();
        int tx = (w - fm.stringWidth(texto)) / 2, ty = (h + fm.getAscent() - fm.getDescent()) / 2;
        g.setColor(Color.BLACK);
        g.drawString(texto, tx + 1, ty + 1);
        g.setColor(Color.WHITE);
        g.drawString(texto, tx, ty);
        g.dispose();
    }
}

package presentacion;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import dominio.Efecto;
import dominio.Jugador;
import dominio.Lado;
import dominio.Partida;
import dominio.Reglas;

/**
 * Clase que dibuja al jugador
 */
public class RenderizadorJugador implements Renderizador {
    @Override
    public void dibujar(Graphics2D g, Partida p) {
        for (Lado l : Lado.values()) dibujarJugador(g, p.getJugador(l));
    }

    private void dibujarJugador(Graphics2D g, Jugador j) {
        long t = System.currentTimeMillis();
        g.setColor(new Color(0, 0, 0, 70));
        g.fill(new Ellipse2D.Double(j.getX() - 34, Reglas.Y_SUELO - 6, 68, 14));

        if (j.tieneAura()) aura(g, j, t);
        DibujoPersonaje.dibujar(g, j.getPersonaje(), j.getX(), j.getY(), 1.0, j.getDireccion(), j.getPieX(), j.getPieY());
        if (j.estaAturdido()) estrellas(g, j, t, new Color(255, 170, 40));
        Efecto ef = j.getEfecto();
        if (ef != null) efecto(g, j, ef, t);
        etiquetaYVida(g, j);
    }

    private void aura(Graphics2D g, Jugador j, long t) {
        Color c = AspectoPoder.color(j.getPersonaje().getPoder().getId());
        double cx = j.getX(), cy = j.getY() - 55;
        double pulso = 1 + 0.08 * Math.sin(t / 110.0);
        for (int k = 3; k >= 1; k--) {
            g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 35 + (3 - k) * 28));
            double r = (46 + k * 12) * pulso;
            g.fill(new Ellipse2D.Double(cx - r, cy - r * 1.25, 2 * r, 2.5 * r));
        }
        g.setStroke(new BasicStroke(3f));
        g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 200));
        double r = 62 * pulso;
        g.draw(new Ellipse2D.Double(cx - r, cy - r * 1.25, 2 * r, 2.5 * r));
        for (int i = 0; i < 6; i++) {                       // chispas que giran alrededor
            double ang = t / 300.0 + i * Math.PI / 3;
            double sx = cx + Math.cos(ang) * r, sy = cy + Math.sin(ang) * r * 1.25;
            g.setColor(new Color(255, 255, 255, 220));
            g.fill(new Ellipse2D.Double(sx - 3, sy - 3, 6, 6));
        }
    }

    private void efecto(Graphics2D g, Jugador j, Efecto ef, long t) {
        double cx = j.getX(), suelo = j.getY(), top = suelo - 112, cy = suelo - 55;
        String id = ef.getId();
        if (id.equals("empuje")) {
            g.setStroke(new BasicStroke(3f));
            g.setColor(new Color(110, 180, 255, 200));
            double lado = j.getDireccion();                 // la estela queda detrás del empuje (hacia el rival)
            for (int i = 0; i < 5; i++) {
                double yy = suelo - 20 - i * 20, largo = 40 + 25 * ((t / 60 + i) % 3);
                g.draw(new Line2D.Double(cx + lado * 40, yy, cx + lado * (40 + largo), yy));
            }
        } else if (id.equals("electrocucion")) {
            g.setColor(new Color(255, 235, 60, 90));
            g.fill(new Ellipse2D.Double(cx - 48, top - 6, 96, 120));
            g.setStroke(new BasicStroke(2.5f));
            g.setColor(new Color(255, 240, 80));
            for (int k = 0; k < 3; k++) {
                Path2D rayo = new Path2D.Double();
                double x0 = cx - 40 + k * 40, y0 = top - 4;
                rayo.moveTo(x0, y0);
                for (int s = 1; s <= 6; s++) {
                    double jit = ((t / 70 + k * 3 + s * 5) % 5 - 2) * 7;
                    rayo.lineTo(x0 + jit, y0 + s * 20);
                }
                g.draw(rayo);
            }
        } else if (id.equals("inconsciente")) {
            estrellas(g, j, t, new Color(120, 200, 255));
            texto(g, "z Z z", new Color(235, 245, 255), cx + 40, top - 10 - (t / 40 % 12), 18);
        } else if (id.equals("ilusion")) {
            for (int i = 0; i < 3; i++) {
                double ang = -t / 260.0 + i * Math.PI * 2 / 3;
                texto(g, "?", new Color(190, 100, 240), cx + Math.cos(ang) * 40, top - 12 + Math.sin(ang) * 10, 26);
            }
        } else {
            g.setColor(new Color(255, 255, 255, 130));
            g.setStroke(new BasicStroke(3f));
            g.draw(new Ellipse2D.Double(cx - 50, top - 8, 100, 124));
        }
    }

    private void estrellas(Graphics2D g, Jugador j, long t, Color c) {
        double top = j.getY() - 112;
        for (int i = 0; i < 3; i++) {
            double ang = t / 220.0 + i * Math.PI * 2 / 3;
            texto(g, "\u2605", c, j.getX() + Math.cos(ang) * 38, top - 8 + Math.sin(ang) * 9, 22);
        }
    }

    private void texto(Graphics2D g, String s, Color c, double cx, double cy, int tam) {
        Font f = Estilos.fuente(Math.max(8, tam));
        g.setFont(f);
        FontMetrics fm = g.getFontMetrics();
        g.setColor(c);
        g.drawString(s, (float) (cx - fm.stringWidth(s) / 2.0), (float) (cy + fm.getAscent() / 2.5));
    }

    private void etiquetaYVida(Graphics2D g, Jugador j) {
        String s = j.esHumano() ? (j.getLado() == Lado.IZQUIERDO ? "J1" : "J2") : "CPU";
        Color c = j.getLado() == Lado.IZQUIERDO ? new Color(60, 120, 240) : new Color(235, 70, 70);
        double cx = j.getX(), top = j.getY() - 112;

        double f = Math.max(0, Math.min(1, j.getVida() / j.getVidaMaxima()));      // barra de vida
        g.setColor(new Color(20, 20, 30, 220));
        g.fill(new RoundRectangle2D.Double(cx - 33, top - 12, 66, 9, 6, 6));
        g.setColor(f > 0.5 ? new Color(90, 220, 90) : f > 0.25 ? new Color(250, 210, 60) : new Color(240, 70, 60));
        g.fill(new RoundRectangle2D.Double(cx - 31, top - 10, 62 * f, 5, 4, 4));

        g.setFont(Estilos.fuente(12));                                              // etiqueta
        int w = g.getFontMetrics().stringWidth(s) + 12;
        double y = top - 22;
        g.setColor(c);
        g.fill(new RoundRectangle2D.Double(cx - w / 2.0, y - 14, w, 18, 8, 8));
        g.setColor(Color.WHITE);
        g.drawString(s, (float) (cx - w / 2.0 + 6), (float) y);
    }
}

package presentacion;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import dominio.Partida;
import dominio.Reglas;

/** Estadio con público y cancha. Se dibuja una sola vez en una imagen */
public class RenderizadorFondo implements Renderizador {
    private static BufferedImage cache;

    public static synchronized BufferedImage estadio() {
        if (cache == null) cache = generar();
        return cache;
    }

    /** Pinta el estadio estirado a w x h (lo usan los menús como fondo). */
    public static void pintarEstadio(Graphics2D g, int w, int h) {
        g.drawImage(estadio(), 0, 0, w, h, null);
    }

    @Override
    public void dibujar(Graphics2D g, Partida p) {
        g.drawImage(estadio(), 0, 0, null);
    }

    private static BufferedImage generar() {
        int w = (int) Reglas.ANCHO, h = (int) Reglas.ALTO;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        Estilos.activarCalidad(g);
        g.setPaint(new GradientPaint(0, 0, new Color(105, 175, 250), 0, 120, new Color(205, 235, 255)));
        g.fillRect(0, 0, w, 130);
        g.setColor(new Color(100, 94, 108));
        g.fillRect(0, 90, w, 280);

        Random r = new Random(7);
        Color[] camisas = { new Color(205, 60, 60), new Color(60, 90, 205), new Color(240, 205, 60),
                new Color(80, 170, 90), new Color(235, 235, 235), new Color(165, 70, 175), new Color(240, 140, 50) };
        Color[] pieles = { new Color(245, 205, 170), new Color(215, 165, 120), new Color(150, 100, 70) };
        for (int fila = 0; fila < 17; fila++) {
            int yy = 104 + fila * 16;
            for (int xx = (fila % 2) * 8; xx < w; xx += 16) {
                g.setColor(camisas[r.nextInt(camisas.length)]);
                g.fill(new Ellipse2D.Double(xx, yy + 5, 12, 12));
                g.setColor(pieles[r.nextInt(pieles.length)]);
                g.fill(new Ellipse2D.Double(xx + 2, yy - 2, 8, 8));
            }
        }
        g.setColor(new Color(40, 40, 55));
        g.fillRect(0, 362, w, 40);
        Color[] vallas = { new Color(200, 40, 50), new Color(30, 90, 190), new Color(235, 185, 30), new Color(40, 150, 80) };
        g.setFont(Estilos.fuente(18));
        for (int i = 0; i < 8; i++) {
            g.setColor(vallas[i % vallas.length]);
            g.fillRect(i * 120 + 4, 366, 112, 32);
            g.setColor(Color.WHITE);
            g.drawString("DOPOSoccer", i * 120 + 14, 389);
        }
        for (int i = 0; i < w; i += 120) {
            g.setColor(i % 240 == 0 ? new Color(82, 152, 62) : new Color(92, 164, 72));
            g.fillRect(i, 402, 120, h - 402);
        }
        g.setColor(new Color(255, 255, 255, 190));
        g.setStroke(new java.awt.BasicStroke(3f));
        g.drawLine(w / 2, 410, w / 2, h - 8);
        g.draw(new java.awt.geom.Ellipse2D.Double(w / 2.0 - 95, Reglas.Y_SUELO - 34, 190, 68));
        g.drawRect(-3, 425, 150, 90);
        g.drawRect(w - 147, 425, 150, 90);
        g.dispose();
        return img;
    }
}

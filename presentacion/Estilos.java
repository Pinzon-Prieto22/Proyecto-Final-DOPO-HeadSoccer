package presentacion;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

/** Colores, fuentes y rutinas de dibujo compartidas para que todas las pantallas se vean iguales. */
public final class Estilos {
    public static final Color DORADO = new Color(244, 190, 40);
    public static final Color MARRON = new Color(70, 45, 10);
    public static final Color AZUL_OSCURO = new Color(20, 30, 65);
    public static final Color FONDO_PANEL = new Color(15, 25, 60, 200);

    private Estilos() { }

    public static Font fuente(int tam) { return new Font(Font.SANS_SERIF, Font.BOLD, tam); }
    public static Font fuenteTitulo(int tam) { return new Font(Font.SANS_SERIF, Font.BOLD | Font.ITALIC, tam); }

    public static void activarCalidad(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }

    /** Dibuja texto centrado en cx con relleno y contorno grueso (estilo del juego original). */
    public static void textoContorno(Graphics2D g, String s, Font f, double cx, double baseY,
                                     Color relleno, Color borde, float grosor) {
        GlyphVector gv = f.createGlyphVector(g.getFontRenderContext(), s);
        Rectangle2D b = gv.getVisualBounds();
        Shape forma = gv.getOutline((float) (cx - b.getWidth() / 2 - b.getX()), (float) baseY);
        g.setStroke(new BasicStroke(grosor, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(borde);
        g.draw(forma);
        g.setColor(relleno);
        g.fill(forma);
    }

    /** Botón dorado redondeado, igual para JButton y JToggleButton. */
    public static void pintarBoton(Graphics2D g, int w, int h, String texto, Font f,
                                   boolean sobre, boolean presionado, boolean activo, boolean seleccionado) {
        activarCalidad(g);
        Color a, b;
        if (!activo) { a = new Color(190, 190, 190); b = new Color(130, 130, 130); }
        else if (seleccionado) { a = new Color(235, 70, 70); b = new Color(150, 20, 30); }
        else { a = new Color(255, 228, 100); b = new Color(228, 150, 12); }
        if (sobre && activo) { a = a.brighter(); b = b.brighter(); }
        int off = presionado ? 2 : 0;
        double arco = h * 0.6;
        g.setColor(new Color(0, 0, 0, 90));
        g.fill(new RoundRectangle2D.Double(3, 5, w - 5, h - 6, arco, arco));
        RoundRectangle2D r = new RoundRectangle2D.Double(2, 2 + off, w - 6, h - 7, arco, arco);
        g.setPaint(new GradientPaint(0, 0, a, 0, h, b));
        g.fill(r);
        g.setColor(MARRON);
        g.setStroke(new BasicStroke(3f));
        g.draw(r);
        g.setColor(new Color(255, 255, 255, 70));
        g.fill(new RoundRectangle2D.Double(9, 5 + off, w - 20, h / 2.0 - 5, h * 0.4, h * 0.4));
        g.setFont(f);
        FontMetrics fm = g.getFontMetrics();
        int x = (w - fm.stringWidth(texto)) / 2;
        int y = (h + fm.getAscent() - fm.getDescent()) / 2 + off;
        g.setColor(!activo ? new Color(90, 90, 90) : seleccionado ? Color.WHITE : MARRON);
        g.drawString(texto, x, y);
    }
}

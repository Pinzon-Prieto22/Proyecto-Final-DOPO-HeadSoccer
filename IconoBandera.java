package presentacion;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.Icon;
/**
 * Para dibujar la bandera del pais
 */
public class IconoBandera implements Icon {
    private final String codigo;
    private final int ancho, alto;

    public IconoBandera(String codigo, int ancho, int alto) {
        this.codigo = codigo;
        this.ancho = ancho;
        this.alto = alto;
    }

    @Override public int getIconWidth() { return ancho; }
    @Override public int getIconHeight() { return alto; }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        Estilos.activarCalidad(g2);
        Bandera.dibujar(g2, codigo, x, y, ancho, alto);
        g2.dispose();
    }
}

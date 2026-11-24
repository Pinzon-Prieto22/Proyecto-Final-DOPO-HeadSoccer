package presentacion;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;
import dominio.Reglas;

/** Una estadística (1 a 10) dibujada como rayitas coloreadas hasta su nivel. */
public class BarraEstadistica extends JComponent {
    private final String etiqueta;
    private int valor = 1;

    public BarraEstadistica(String etiqueta) {
        this.etiqueta = etiqueta;
        setPreferredSize(new Dimension(230, 22));
    }

    public void setValor(int valor) { this.valor = valor; repaint(); }

    @Override
    protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0.create();
        Estilos.activarCalidad(g);
        g.setFont(Estilos.fuente(12));
        FontMetrics fm = g.getFontMetrics();
        int base = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
        g.setColor(Color.WHITE);
        g.drawString(etiqueta, 2, base);
        String num = valor + "/" + Reglas.NIVEL_MAXIMO;
        g.drawString(num, getWidth() - fm.stringWidth(num) - 2, base);
        int x0 = 66, x1 = getWidth() - fm.stringWidth(num) - 8, n = Reglas.NIVEL_MAXIMO;
        int ancho = Math.max(4, (x1 - x0) / n);
        for (int i = 0; i < n; i++) {
            g.setColor(i < valor ? Estilos.DORADO : new Color(40, 60, 110));
            g.fillRoundRect(x0 + i * ancho, 4, ancho - 3, getHeight() - 8, 4, 4);
        }
        g.dispose();
    }
}

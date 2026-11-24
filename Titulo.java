package presentacion;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;
/**
 * Muestra el texto grande con el estilo del juego
 */
public class Titulo extends JComponent {
    private String texto;
    private final int tamano;

    public Titulo(String texto, int tamano) {
        this.texto = texto;
        this.tamano = tamano;
        setPreferredSize(new Dimension(tamano * 12, (int) (tamano * 1.5)));
    }

    public void setTexto(String t) { this.texto = t; repaint(); }

    @Override
    protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0.create();
        Estilos.activarCalidad(g);
        Estilos.textoContorno(g, texto, Estilos.fuenteTitulo(tamano), getWidth() / 2.0,
                getHeight() / 2.0 + tamano * 0.35, new Color(255, 226, 90), Estilos.MARRON, Math.max(4f, tamano / 6f));
        g.dispose();
    }
}

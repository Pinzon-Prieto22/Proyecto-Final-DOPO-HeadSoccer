package presentacion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;
import javax.swing.JPanel;

/** Base de todas las pantallas: un JPanel con el estadio de fondo y valores por defecto para alMostrar/alOcultar. */
public abstract class PantallaBase extends JPanel implements Pantalla {
    protected final Navegador navegador;

    protected PantallaBase(Navegador navegador) {
        super(new BorderLayout());
        this.navegador = navegador;
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0.create();
        Estilos.activarCalidad(g);
        RenderizadorFondo.pintarEstadio(g, getWidth(), getHeight());
        g.setColor(new Color(0, 0, 0, 70));
        g.fillRect(0, 0, getWidth(), getHeight());
        g.dispose();
    }

    @Override public JComponent getVista() { return this; }
    @Override public void alMostrar() { }
    @Override public void alOcultar() { }
}

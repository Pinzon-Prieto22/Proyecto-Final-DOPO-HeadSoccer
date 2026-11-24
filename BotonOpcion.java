package presentacion;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JToggleButton;

public class BotonOpcion extends JToggleButton {
    public BotonOpcion(String texto) {
        super(texto);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setFont(Estilos.fuente(16));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(200, 46));
    }

    @Override
    protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0.create();
        Estilos.pintarBoton(g, getWidth(), getHeight(), getText(), getFont(),
                getModel().isRollover(), getModel().isPressed(), isEnabled(), isSelected());
        g.dispose();
    }
}

package presentacion;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JButton;

public class BotonDorado extends JButton {
    public BotonDorado(String texto) {
        super(texto);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setFont(Estilos.fuente(18));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(230, 52));
    }

    @Override
    protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0.create();
        Estilos.pintarBoton(g, getWidth(), getHeight(), getText(), getFont(),
                getModel().isRollover(), getModel().isPressed(), isEnabled(), false);
        g.dispose();
    }
}

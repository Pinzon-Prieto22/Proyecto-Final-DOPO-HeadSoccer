package presentacion;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import javax.swing.JPanel;

public class PanelTranslucido extends JPanel {
    public PanelTranslucido() { this(null); }

    public PanelTranslucido(LayoutManager layout) {
        super(layout);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0.create();
        Estilos.activarCalidad(g);
        g.setColor(Estilos.FONDO_PANEL);
        g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 28, 28);
        g.setColor(new Color(255, 255, 255, 110));
        g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 28, 28);
        g.dispose();
        super.paintComponent(g0);
    }
}

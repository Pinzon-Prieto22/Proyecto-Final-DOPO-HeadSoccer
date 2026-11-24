package presentacion;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * Pantalla que muestra como se juega
 */
public class PantallaAyuda extends PantallaBase {
    public PantallaAyuda(final Navegador navegador) {
        super(navegador);
        setBorder(new EmptyBorder(14, 60, 20, 60));
        add(new Titulo("C\u00d3MO JUGAR", 44), BorderLayout.NORTH);
        JPanel centro = new JPanel(new java.awt.GridBagLayout());
        centro.setOpaque(false);
        centro.add(new PanelAyuda());
        add(centro, BorderLayout.CENTER);
        BotonDorado volver = new BotonDorado("VOLVER");
        volver.addActionListener(e -> navegador.ir(IdPantalla.INICIO));
        JPanel sur = new JPanel(new FlowLayout(FlowLayout.CENTER));
        sur.setOpaque(false);
        sur.add(volver);
        add(sur, BorderLayout.SOUTH);
    }

    @Override public IdPantalla getId() { return IdPantalla.AYUDA; }
}

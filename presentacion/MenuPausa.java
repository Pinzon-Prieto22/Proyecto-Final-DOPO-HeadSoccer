package presentacion;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * Menú de pausa. Se instala como "glass pane" del JFrame: queda por encima de todo, oscurece el partido y bloquea
 * los clics hacia la cancha mientras está visible.
 */
public class MenuPausa extends JPanel {
    public MenuPausa(Runnable reanudar, Runnable guardar, Runnable terminar, Runnable ayuda, Runnable salir) {
        super(new GridBagLayout());
        setOpaque(false);
        PanelTranslucido caja = new PanelTranslucido(new GridLayout(0, 1, 0, 11));
        caja.setBorder(new EmptyBorder(20, 46, 24, 46));

        JLabel titulo = new JLabel("PAUSA", SwingConstants.CENTER);
        titulo.setFont(Estilos.fuenteTitulo(44));
        titulo.setForeground(Estilos.DORADO);
        caja.add(titulo);
        agregar(caja, "REANUDAR", reanudar);
        agregar(caja, "GUARDAR PARTIDA", guardar);
        agregar(caja, "TERMINAR PARTIDA", terminar);
        agregar(caja, "C\u00d3MO JUGAR", ayuda);
        agregar(caja, "SALIR AL MEN\u00da", salir);
        add(caja);

        addMouseListener(new MouseAdapter() { });
        addMouseMotionListener(new MouseMotionAdapter() { });
    }

    private static void agregar(JPanel caja, String texto, final Runnable accion) {
        BotonDorado b = new BotonDorado(texto);
        b.addActionListener(e -> accion.run());
        caja.add(b);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}

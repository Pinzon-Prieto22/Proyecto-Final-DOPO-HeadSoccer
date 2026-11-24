package presentacion;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import dominio.Lado;
import dominio.Partida;
import dominio.TipoPartida;

/**
 * Franja superior de la pantalla de juego (información abierta): datos de cada jugador a los lados
 * y, en el centro, el tipo de partido, el tiempo y el marcador.
 */
public class PanelHUD extends JPanel {
    private final PanelJugadorHUD izquierdo = new PanelJugadorHUD(Lado.IZQUIERDO, "Q");
    private final PanelJugadorHUD derecho = new PanelJugadorHUD(Lado.DERECHO, "Ctrl der.");
    private final JLabel lblTipo = etiqueta(15, new Color(190, 210, 255));
    private final JLabel lblTiempo = etiqueta(38, Color.WHITE);
    private final JLabel lblMarcador = etiqueta(26, Estilos.DORADO);
    private final JLabel lblAviso = etiqueta(13, new Color(180, 190, 220));

    public PanelHUD() {
        super(new GridBagLayout());
        setBackground(new Color(10, 18, 45));
        setBorder(new EmptyBorder(6, 8, 6, 8));

        JPanel centro = new JPanel(new GridLayout(0, 1));
        centro.setOpaque(false);
        centro.add(lblTipo);
        centro.add(lblTiempo);
        centro.add(lblMarcador);
        centro.add(lblAviso);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;
        c.insets = new Insets(0, 4, 0, 4);
        c.weightx = 0.38; c.gridx = 0; add(izquierdo, c);
        c.weightx = 0.24; c.gridx = 1; add(centro, c);
        c.weightx = 0.38; c.gridx = 2; add(derecho, c);
    }

    private static JLabel etiqueta(int tam, Color color) {
        JLabel l = new JLabel(" ", SwingConstants.CENTER);
        l.setFont(Estilos.fuente(tam));
        l.setForeground(color);
        return l;
    }

    public void actualizar(Partida p) {
        izquierdo.actualizar(p);
        derecho.actualizar(p);
        TipoPartida t = p.getTipo();
        lblTipo.setText(t.getNombre());
        double r = t.getTiempoRestante();
        if (r < 0) {
            lblTiempo.setText("\u221e");
        } else {
            int s = (int) Math.ceil(r);
            lblTiempo.setText(String.format("%d:%02d", s / 60, s % 60));
        }
        lblMarcador.setText(p.getMarcador().getGoles(Lado.IZQUIERDO) + "  :  " + p.getMarcador().getGoles(Lado.DERECHO));
        lblAviso.setText(t.enMuerteSubita() ? "\u00a1GOL DE ORO!" : "ESC / P: pausa");
    }
}

package presentacion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import dominio.Efecto;
import dominio.Estadisticas;
import dominio.Jugador;
import dominio.Lado;
import dominio.Partida;
import dominio.Personaje;
import dominio.TipoEstadistica;

/**
 * Información de un jugador: identificación (personaje y país), goles, vida, barra de poder, poder/aura activa,
 * efecto recibido, estadísticas y puntos acumulados. Es un panel Swing hecho con JLabel y barras propias.
 */
public class PanelJugadorHUD extends PanelTranslucido {
    private final Lado lado;
    private final JLabel lblBandera = new JLabel();
    private final JLabel lblNombre = etiqueta(15, Color.WHITE);
    private final JLabel lblGoles = etiqueta(34, Estilos.DORADO);
    private final JLabel lblPoder = etiqueta(12, new Color(190, 210, 255));
    private final JLabel lblEstado = etiqueta(13, new Color(255, 130, 130));
    private final JLabel lblStats = etiqueta(12, new Color(200, 215, 240));
    private final BarraVida barraVida;
    private final BarraPoder barraPoder;
    private Personaje mostrado;

    public PanelJugadorHUD(Lado lado, String tecla) {
        super(new BorderLayout(6, 3));
        this.lado = lado;
        boolean izq = lado == Lado.IZQUIERDO;
        int alin = izq ? SwingConstants.LEFT : SwingConstants.RIGHT;
        for (JLabel l : new JLabel[] { lblNombre, lblPoder, lblEstado, lblStats }) l.setHorizontalAlignment(alin);
        setBorder(new EmptyBorder(5, 12, 5, 12));
        barraVida = new BarraVida(!izq);
        barraPoder = new BarraPoder(!izq, tecla);

        JPanel identidad = new JPanel(new FlowLayout(izq ? FlowLayout.LEFT : FlowLayout.RIGHT, 8, 0));
        identidad.setOpaque(false);
        if (izq) { identidad.add(lblBandera); identidad.add(lblNombre); }
        else { identidad.add(lblNombre); identidad.add(lblBandera); }

        JPanel fila = new JPanel(new BorderLayout(10, 0));
        fila.setOpaque(false);
        fila.add(identidad, BorderLayout.CENTER);
        fila.add(lblGoles, izq ? BorderLayout.EAST : BorderLayout.WEST);

        JPanel barras = new JPanel(new GridLayout(2, 1, 0, 3));
        barras.setOpaque(false);
        barras.add(barraVida);
        barras.add(barraPoder);

        JPanel info = new JPanel(new GridLayout(0, 1));
        info.setOpaque(false);
        info.add(lblPoder);
        info.add(lblEstado);
        info.add(lblStats);

        add(fila, BorderLayout.NORTH);
        add(barras, BorderLayout.CENTER);
        add(info, BorderLayout.SOUTH);
    }

    private static JLabel etiqueta(int tam, Color color) {
        JLabel l = new JLabel(" ");
        l.setFont(Estilos.fuente(tam));
        l.setForeground(color);
        return l;
    }

    /** Refresca los datos con el estado actual de la partida. */
    public void actualizar(Partida p) {
        Jugador j = p.getJugador(lado);
        Personaje per = j.getPersonaje();
        if (mostrado != per) {
            mostrado = per;
            lblBandera.setIcon(new IconoBandera(per.getEquipo().getCodigo(), 44, 30));
            lblNombre.setText(j.getNombre() + " \u00b7 " + per.getNombre() + " (" + per.getEquipo().getPais() + ")");
        }
        lblGoles.setText(String.valueOf(p.getMarcador().getGoles(lado)));
        barraVida.setValores(j.getVida(), j.getVidaMaxima(), j.estaAturdido());
        barraPoder.setEstado(j.getMedidor().getPorcentaje(), j.tieneAura(), AspectoPoder.color(per.getPoder().getId()));
        lblPoder.setText("Poder: " + per.getPoder().getNombre() + "   \u00b7   Aura: " + (j.tieneAura() ? "ACTIVA" : "\u2014"));

        Efecto e = j.getEfecto();
        if (j.estaAturdido()) lblEstado.setText("Aturdido " + String.format("%.1f s", j.getTiempoAturdido()));
        else if (e != null) lblEstado.setText(e.getNombre() + " " + String.format("%.1f s", e.getTiempoRestante()));
        else lblEstado.setText(" ");

        Estadisticas s = j.getEstadisticas();
        lblStats.setText("SPD " + s.getNivel(TipoEstadistica.SPEED) + " \u00b7 KICK " + s.getNivel(TipoEstadistica.KICK)
                + " \u00b7 JMP " + s.getNivel(TipoEstadistica.JUMP) + " \u00b7 SRV " + s.getNivel(TipoEstadistica.SURVIVAL)
                + " \u00b7 Puntos: " + (j.esHumano() ? String.valueOf(j.getPuntos()) : "\u2014"));
    }
}

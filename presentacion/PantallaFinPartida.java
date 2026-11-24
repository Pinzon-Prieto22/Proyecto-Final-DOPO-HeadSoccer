package presentacion;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import dominio.DOPOSoccerException;
import dominio.FabricaPartida;
import dominio.Lado;
import dominio.Perfil;
import dominio.ResultadoPartida;

/** Muestra el ganador y los puntos obtenidos, y ofrece revancha, cambiar jugadores/mejorar estadísticas o volver al menú. */
public class PantallaFinPartida extends PantallaBase {
    private final PanelTranslucido caja = new PanelTranslucido(new GridBagLayout());
    private final JPanel centro = new JPanel(new GridBagLayout());

    public PantallaFinPartida(Navegador navegador) {
        super(navegador);
        centro.setOpaque(false);
        caja.setBorder(new EmptyBorder(24, 44, 24, 44));
        centro.add(caja);
        add(centro);
    }

    private int y;

    private void linea(String texto, int tam, Color color) {
        JLabel l = new JLabel(texto, SwingConstants.CENTER);
        l.setFont(Estilos.fuente(tam));
        l.setForeground(color);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0; c.gridy = y++; c.insets = new Insets(5, 6, 5, 6);
        caja.add(l, c);
    }

    @Override
    public void alMostrar() {
        final Navegador nav = navegador;
        final ContextoJuego ctx = nav.getContexto();
        ResultadoPartida r = ctx.getUltimoResultado();
        caja.removeAll();
        y = 0;

        if (r.fueTerminadaPorUsuario()) {
            linea("PARTIDA TERMINADA", 42, Estilos.DORADO);
            linea("Se termin\u00f3 sin ganador. No se otorgaron puntos.", 18, Color.WHITE);
        } else {
            Lado g = r.getGanador();
            linea("\u00a1GANA " + r.getNombreJugador(g).toUpperCase() + "!", 42, Estilos.DORADO);
            linea(r.getPersonaje(g).getNombre() + " \u00b7 " + r.getPersonaje(g).getEquipo().getPais(), 18, new Color(150, 220, 255));
        }
        linea(r.getNombreJugador(Lado.IZQUIERDO) + "   " + r.getGoles(Lado.IZQUIERDO) + "  :  " + r.getGoles(Lado.DERECHO)
                + "   " + r.getNombreJugador(Lado.DERECHO), 26, Color.WHITE);
        linea("Duraci\u00f3n: " + (int) r.getDuracion() + " s" + (r.huboMuerteSubita() ? "  \u00b7  Se defini\u00f3 en muerte s\u00fabita" : ""), 14, new Color(190, 210, 255));

        if (!r.fueTerminadaPorUsuario()) {
            for (Lado l : Lado.values()) {
                if (!r.esHumano(l)) continue;
                Perfil p = ctx.getRegistro().buscar(r.getNombreJugador(l));
                linea("Puntos obtenidos por " + r.getNombreJugador(l) + ": +" + r.getPuntosGanados(l)
                        + (p != null ? "   (total: " + p.getPuntos() + ")" : ""), 18, new Color(150, 255, 170));
            }
        }

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 0));
        botones.setOpaque(false);
        if (!ctx.esPartidaAbierta()) {
            BotonDorado revancha = new BotonDorado("REVANCHA");
            revancha.addActionListener(e -> {
                try {
                    ctx.setPartida(FabricaPartida.crear(ctx.getConfiguracion()), false);
                    nav.ir(IdPantalla.JUEGO);
                } catch (DOPOSoccerException ex) {
                    Mensajes.error(this, "No se puede iniciar la revancha", ex);
                }
            });
            BotonDorado cambiar = new BotonDorado("MEJORAR / CAMBIAR");
            cambiar.addActionListener(e -> nav.ir(IdPantalla.SELECCION));
            botones.add(revancha);
            botones.add(cambiar);
        }
        BotonDorado menu = new BotonDorado("MEN\u00da PRINCIPAL");
        menu.addActionListener(e -> nav.ir(IdPantalla.INICIO));
        botones.add(menu);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0; c.gridy = y++; c.insets = new Insets(18, 6, 4, 6);
        caja.add(botones, c);
        caja.revalidate();
        caja.repaint();
    }

    @Override public IdPantalla getId() { return IdPantalla.FIN; }
}

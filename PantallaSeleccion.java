package presentacion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import dominio.ConfiguracionJuego;
import dominio.DOPOSoccerException;
import dominio.FabricaPartida;
import dominio.Lado;
import dominio.Partida;
/**
 * Pantalla destinada a la selleccion de pj
 */
public class PantallaSeleccion extends PantallaBase {
    private final SelectorJugador izquierdo;
    private final SelectorJugador derecho;

    public PantallaSeleccion(final Navegador navegador) {
        super(navegador);
        setBorder(new EmptyBorder(10, 24, 16, 24));
        izquierdo = new SelectorJugador(Lado.IZQUIERDO);
        derecho = new SelectorJugador(Lado.DERECHO);
        add(new Titulo("ELIGE TUS JUGADORES", 40), BorderLayout.NORTH);

        JLabel vs = new JLabel("VS");
        vs.setFont(Estilos.fuenteTitulo(42));
        vs.setForeground(new Color(255, 235, 60));
        JPanel centro = new JPanel(new GridBagLayout());
        centro.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH; c.weightx = 1; c.weighty = 1; c.insets = new Insets(0, 6, 0, 6);
        c.gridx = 0; centro.add(izquierdo, c);
        c.gridx = 1; c.weightx = 0; c.fill = GridBagConstraints.NONE; centro.add(vs, c);
        c.gridx = 2; c.weightx = 1; c.fill = GridBagConstraints.BOTH; centro.add(derecho, c);
        add(centro, BorderLayout.CENTER);

        BotonDorado volver = new BotonDorado("VOLVER");
        BotonDorado jugar = new BotonDorado("JUGAR");
        volver.addActionListener(e -> navegador.ir(IdPantalla.CONFIGURACION));
        jugar.addActionListener(e -> jugar());
        JPanel sur = new JPanel(new FlowLayout(FlowLayout.CENTER, 24, 0));
        sur.setOpaque(false);
        sur.add(volver);
        sur.add(jugar);
        add(sur, BorderLayout.SOUTH);
    }

    private void jugar() {
        ConfiguracionJuego cfg = navegador.getContexto().getConfiguracion();
        cfg.setPersonaje(Lado.IZQUIERDO, izquierdo.getPersonaje());
        cfg.setPersonaje(Lado.DERECHO, derecho.getPersonaje());
        try {
            Partida p = FabricaPartida.crear(cfg);
            navegador.getContexto().setPartida(p);
            navegador.ir(IdPantalla.JUEGO);
        } catch (DOPOSoccerException e) {
            Mensajes.error(this, "No se puede iniciar el partido", e);
        }
    }

    private static String titulo(Lado lado) {
        String n = lado == Lado.IZQUIERDO ? "JUGADOR 1" : "JUGADOR 2";
        String[] t = EsquemaTeclas.para(lado).getTextos();
        return n + " \u2014 " + t[0] + " " + t[1] + " mover \u00b7 " + t[2] + " saltar \u00b7 " + t[3] + " patear \u00b7 " + t[4] + " poder";
    }

    @Override
    public void alMostrar() {
        ConfiguracionJuego cfg = navegador.getContexto().getConfiguracion();
        izquierdo.setPersonaje(cfg.getPersonaje(Lado.IZQUIERDO));
        derecho.setPersonaje(cfg.getPersonaje(Lado.DERECHO));
        izquierdo.setTitulo(titulo(Lado.IZQUIERDO));
        derecho.setTitulo(titulo(Lado.DERECHO));
    }

    @Override public IdPantalla getId() { return IdPantalla.SELECCION; }
}

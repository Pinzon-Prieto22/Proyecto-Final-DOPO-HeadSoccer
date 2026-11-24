package presentacion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import dominio.ConfiguracionJuego;
import dominio.DOPOSoccerException;
import dominio.FabricaPartida;
import dominio.Lado;
import dominio.Partida;
import dominio.Perfil;
import dominio.RegistroPerfiles;

/** Cada jugador elige (o crea) su perfil, mejora sus estadísticas con sus puntos y escoge su personaje. */
public class PantallaSeleccion extends PantallaBase {
    private final SelectorJugador izquierdo;
    private final SelectorJugador derecho;

    public PantallaSeleccion(final Navegador navegador) {
        super(navegador);
        setBorder(new EmptyBorder(10, 24, 16, 24));
        RegistroPerfiles registro = navegador.getContexto().getRegistro();
        izquierdo = new SelectorJugador(Lado.IZQUIERDO, registro);
        derecho = new SelectorJugador(Lado.DERECHO, registro);
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
        cfg.setPerfil(Lado.IZQUIERDO, izquierdo.getPerfil());
        cfg.setPerfil(Lado.DERECHO, cfg.getModalidad().esHumano(Lado.DERECHO) ? derecho.getPerfil() : null);
        try {
            Partida p = FabricaPartida.crear(cfg);
            navegador.getContexto().setPartida(p, false);
            navegador.ir(IdPantalla.JUEGO);
        } catch (DOPOSoccerException e) {
            Mensajes.error(this, "No se puede iniciar el partido", e);
        }
    }

    /** Sin perfiles no se puede jugar: pide crear el primero. @return false si el usuario cancela */
    private boolean pedirPrimerPerfil() {
        RegistroPerfiles reg = navegador.getContexto().getRegistro();
        while (reg.estaVacio()) {
            String nombre = JOptionPane.showInputDialog(this, "Para jugar necesitas un perfil. Escribe tu nombre (1 a 16 caracteres):",
                    "Crear tu primer perfil", JOptionPane.QUESTION_MESSAGE);
            if (nombre == null) return false;
            try {
                reg.crear(nombre);
            } catch (DOPOSoccerException e) {
                Mensajes.error(this, "No se pudo crear el perfil", e);
            }
        }
        return true;
    }

    private static String titulo(Lado lado, boolean humano) {
        String n = lado == Lado.IZQUIERDO ? "JUGADOR 1" : "JUGADOR 2";
        if (!humano) return n + " \u2014 M\u00c1QUINA";
        String[] t = EsquemaTeclas.para(lado).getTextos();
        return n + " \u2014 " + t[0] + " " + t[1] + " mover \u00b7 " + t[2] + " saltar \u00b7 " + t[3] + " patear \u00b7 " + t[4] + " poder";
    }

    @Override
    public void alMostrar() {
        if (!pedirPrimerPerfil()) { navegador.ir(IdPantalla.CONFIGURACION); return; }
        ConfiguracionJuego cfg = navegador.getContexto().getConfiguracion();
        List<Perfil> perfiles = navegador.getContexto().getRegistro().getTodos();
        izquierdo.setPersonaje(cfg.getPersonaje(Lado.IZQUIERDO));
        derecho.setPersonaje(cfg.getPersonaje(Lado.DERECHO));
        izquierdo.configurarHumano(titulo(Lado.IZQUIERDO, true));
        izquierdo.recargarPerfiles(cfg.getPerfil(Lado.IZQUIERDO));
        if (cfg.getModalidad().esHumano(Lado.DERECHO)) {
            derecho.configurarHumano(titulo(Lado.DERECHO, true));
            Perfil previo = cfg.getPerfil(Lado.DERECHO);
            derecho.recargarPerfiles(previo != null ? previo : (perfiles.size() > 1 ? perfiles.get(1) : null));
        } else {
            derecho.configurarMaquina(titulo(Lado.DERECHO, false), cfg.getMaquina());
        }
    }

    @Override public IdPantalla getId() { return IdPantalla.SELECCION; }
}

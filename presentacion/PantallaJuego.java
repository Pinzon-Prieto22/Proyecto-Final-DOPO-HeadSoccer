package presentacion;

import java.awt.BorderLayout;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import dominio.DOPOSoccerException;
import dominio.EstadoPartida;
import dominio.Partida;
import dominio.PartidaListener;
import dominio.ResultadoPartida;

/**
 * Pantalla del partido: HUD arriba (estado del juego e información de cada jugador), cancha al centro y menú de
 * pausa como glass pane. Escucha a la Partida (Observer) para saber cuándo terminó.
 */
public class PantallaJuego extends PantallaBase implements PartidaListener {
    private static final int ANCHO_MENUS = 1100, ALTO_MENUS = 720;
    private Partida partida;
    private PanelHUD hud;
    private PanelCancha cancha;
    private BucleJuego bucle;
    private ControlTeclado teclado;
    private MenuPausa menu;

    public PantallaJuego(Navegador navegador) {
        super(navegador);
    }

    @Override
    public void alMostrar() {
        ContextoJuego ctx = navegador.getContexto();
        partida = ctx.getPartida();
        if (partida == null) { navegador.ir(IdPantalla.INICIO); return; }
        removeAll();
        hud = new PanelHUD();
        cancha = new PanelCancha(partida);
        add(hud, BorderLayout.NORTH);
        add(cancha, BorderLayout.CENTER);
        Resolucion r = ctx.getResolucion();
        navegador.ajustarTamano(r.getAncho(), r.getAlto() + hud.getPreferredSize().height);

        menu = new MenuPausa(this::reanudar, this::guardar, this::terminarPartida, this::mostrarAyuda, this::salirAlMenu);
        partida.agregarListener(this);
        teclado = new ControlTeclado(partida, navegador.getVentana(), this::alternarPausa);
        teclado.activar();
        bucle = new BucleJuego(partida, this::alCuadro);
        hud.actualizar(partida);
        revalidate();
        repaint();
        bucle.iniciar();
        if (partida.getEstado() == EstadoPartida.PAUSADA) mostrarMenu();   // partida abierta desde un archivo
    }

    @Override
    public void alOcultar() {
        detenerTodo();
        navegador.ajustarTamano(ANCHO_MENUS, ALTO_MENUS);
    }

    private void alCuadro() {
        hud.actualizar(partida);
        cancha.repaint();
    }

    private void detenerTodo() {
        if (bucle != null) bucle.detener();
        if (teclado != null) teclado.desactivar();
        if (partida != null) partida.quitarListener(this);
        if (menu != null) menu.setVisible(false);
    }

    // ---------------- Pausa ----------------

    private void alternarPausa() {
        if (partida == null) return;
        EstadoPartida e = partida.getEstado();
        if (e == EstadoPartida.PAUSADA) reanudar();
        else if (e != EstadoPartida.FINALIZADA) {
            partida.pausar();
            teclado.soltarTodo();
            mostrarMenu();
        }
    }

    private void mostrarMenu() {
        JFrame f = navegador.getVentana();
        f.setGlassPane(menu);
        menu.setVisible(true);
    }

    private void reanudar() {
        menu.setVisible(false);
        partida.reanudar();
    }

    private void guardar() {
        File f = SelectorArchivo.guardar(navegador.getVentana());
        if (f == null) return;
        try {
            navegador.getContexto().getRepositorioPartidas().guardar(partida, f);
            Mensajes.info(navegador.getVentana(), "Partida guardada", "La partida se guard\u00f3 en \"" + f.getName() + "\". Puedes abrirla desde el men\u00fa principal.");
        } catch (DOPOSoccerException e) {
            Mensajes.error(navegador.getVentana(), "No se pudo guardar la partida", e);
        }
    }

    /** Los jugadores pueden terminar el juego en cualquier momento: termina sin ganador y sin puntos. */
    private void terminarPartida() {
        if (!Mensajes.confirmar(navegador.getVentana(), "Terminar partida",
                "\u00bfSeguro que quieres terminar el partido? Terminar\u00e1 sin ganador y no se otorgar\u00e1n puntos.")) return;
        menu.setVisible(false);
        partida.terminarManualmente();
    }

    private void mostrarAyuda() {
        JOptionPane.showMessageDialog(navegador.getVentana(), new PanelAyuda(), "C\u00f3mo jugar", JOptionPane.PLAIN_MESSAGE);
    }

    private void salirAlMenu() {
        detenerTodo();
        navegador.ir(IdPantalla.INICIO);
    }

    private void terminar() {
        if (partida == null || partida.getEstado() != EstadoPartida.FINALIZADA) return;
        Partida p = partida;
        detenerTodo();
        ResultadoPartida r = p.getResultado();
        ContextoJuego ctx = navegador.getContexto();
        ctx.setUltimoResultado(r);
        try {
            ctx.getRegistro().registrarResultado(r);      // 100 puntos al ganador y 25 al perdedor
        } catch (DOPOSoccerException e) {
            Mensajes.error(navegador.getVentana(), "No se pudo guardar el progreso", e);
        }
        navegador.ir(IdPantalla.FIN);
    }

    // ---------------- Eventos de la partida ----------------

    @Override
    public void alCambiarEstado(EstadoPartida nuevo) {
        if (nuevo == EstadoPartida.FINALIZADA) SwingUtilities.invokeLater(this::terminar);
    }

    @Override public IdPantalla getId() { return IdPantalla.JUEGO; }
}

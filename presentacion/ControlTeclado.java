package presentacion;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.util.EnumMap;
import java.util.Map;
import dominio.Accion;
import dominio.Controlador;
import dominio.ControladorHumano;
import dominio.EstadoPartida;
import dominio.Lado;
import dominio.Partida;

/**
 * Traduce el teclado a acciones de los ControladorHumano. Usa un KeyEventDispatcher global (AWT) y no
 * InputMap, porque así reconoce el Ctrl derecho y no depende de qué componente tiene el foco.
 */
public class ControlTeclado implements KeyEventDispatcher {
    private final Map<Lado, ControladorHumano> humanos = new EnumMap<Lado, ControladorHumano>(Lado.class);
    private final Partida partida;
    private final Window ventana;
    private final Runnable alternarPausa;
    private boolean teclaPausaAbajo;

    public ControlTeclado(Partida partida, Window ventana, Runnable alternarPausa) {
        this.partida = partida;
        this.ventana = ventana;
        this.alternarPausa = alternarPausa;
        for (Lado l : Lado.values()) {
            Controlador c = partida.getJugador(l).getControlador();
            if (c instanceof ControladorHumano) humanos.put(l, (ControladorHumano) c);
        }
    }

    public void activar() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
    }

    public void desactivar() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this);
        soltarTodo();
    }

    public void soltarTodo() {
        for (ControladorHumano h : humanos.values()) h.soltarTodas();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        if (ventana != null && !ventana.isActive()) return false;
        int id = e.getID();
        if (id != KeyEvent.KEY_PRESSED && id != KeyEvent.KEY_RELEASED) return false;
        boolean presionada = id == KeyEvent.KEY_PRESSED;
        int codigo = e.getKeyCode();

        if (codigo == KeyEvent.VK_ESCAPE || codigo == KeyEvent.VK_P) {
            if (presionada && !teclaPausaAbajo) alternarPausa.run();
            teclaPausaAbajo = presionada;
            return true;
        }
        boolean pausada = partida.getEstado() == EstadoPartida.PAUSADA;
        for (Map.Entry<Lado, ControladorHumano> en : humanos.entrySet()) {
            Accion a = EsquemaTeclas.para(en.getKey()).accion(codigo, e.getKeyLocation());
            if (a == null) continue;
            if (presionada) { if (!pausada) en.getValue().presionar(a); }
            else en.getValue().soltar(a);
            return true;
        }
        return false;
    }
}

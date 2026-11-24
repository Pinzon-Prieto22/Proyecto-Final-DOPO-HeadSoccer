package presentacion;

import java.awt.event.KeyEvent;
import dominio.Accion;
import dominio.Lado;

/** Qué tecla hace qué para cada jugador (ambos comparten el mismo teclado). */
public final class EsquemaTeclas {
    private static final EsquemaTeclas J1 = new EsquemaTeclas(KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_W,
            KeyEvent.VK_S, KeyEvent.VK_Q, false, new String[] { "A", "D", "W", "S", "Q" });
    private static final EsquemaTeclas J2 = new EsquemaTeclas(KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_UP,
            KeyEvent.VK_DOWN, KeyEvent.VK_CONTROL, true, new String[] { "\u2190", "\u2192", "\u2191", "\u2193", "Ctrl derecho" });

    private final int izquierda, derecha, salto, patada, poder;
    private final boolean poderSoloCtrlDerecho;
    private final String[] textos;

    private EsquemaTeclas(int izq, int der, int salto, int patada, int poder, boolean soloDerecho, String[] textos) {
        this.izquierda = izq; this.derecha = der; this.salto = salto; this.patada = patada; this.poder = poder;
        this.poderSoloCtrlDerecho = soloDerecho;
        this.textos = textos;
    }

    public static EsquemaTeclas para(Lado lado) { return lado == Lado.IZQUIERDO ? J1 : J2; }

    /** La acción que corresponde a esa tecla, o null si no es de este jugador. */
    public Accion accion(int codigo, int ubicacion) {
        if (codigo == izquierda) return Accion.IZQUIERDA;
        if (codigo == derecha) return Accion.DERECHA;
        if (codigo == salto) return Accion.SALTAR;
        if (codigo == patada) return Accion.PATEAR;
        if (codigo == poder) {
            if (poderSoloCtrlDerecho && ubicacion != KeyEvent.KEY_LOCATION_RIGHT) return null;
            return Accion.PODER;
        }
        return null;
    }

    /** Textos para mostrar: izquierda, derecha, saltar, patear, poder. */
    public String[] getTextos() { return textos.clone(); }
}

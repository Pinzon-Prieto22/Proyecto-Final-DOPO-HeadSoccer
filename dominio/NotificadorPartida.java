package dominio;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Clase NotificadorPartida que implementa partidaListener
 * Responsable de repartir cada evento entre todos los oyentes registrados
 * 
 * @author Bryan Pinzon y Valentina Prieto y ayuda de IA
 * @version Entrega Final Proyecto Final
 */

public class NotificadorPartida implements PartidaListener {
    // se guarda quienes quieren saber cosas
    private final List<PartidaListener> listeners = new CopyOnWriteArrayList<PartidaListener>(); // CopyOnWriteArrayList para poder usarlo mientra otro hilo tambien
    // metodos para agregar y quitar listeners
    public void agregar(PartidaListener l) { listeners.add(l); }
    public void quitar(PartidaListener l) { listeners.remove(l); }
    // metodos de notificacion
    @Override 
    public void alCambiarEstado(EstadoPartida n) { for (PartidaListener l : listeners) l.alCambiarEstado(n); }
    @Override 
    public void alGol(Lado a, boolean autogol) { for (PartidaListener l : listeners) l.alGol(a, autogol); }
    @Override 
    public void alPatada(Jugador j) { for (PartidaListener l : listeners) l.alPatada(j); }
    @Override 
    public void alActivarPoder(Jugador j) { for (PartidaListener l : listeners) l.alActivarPoder(j); }
    @Override 
    public void alLanzarPoder(Jugador j) { for (PartidaListener l : listeners) l.alLanzarPoder(j); }
    @Override 
    public void alRecibirDanio(Jugador v, double d) { for (PartidaListener l : listeners) l.alRecibirDanio(v, d); }
    @Override 
    public void alAplicarEfecto(Jugador v, Efecto e) { for (PartidaListener l : listeners) l.alAplicarEfecto(v, e); }
    @Override 
    public void alAturdirse(Jugador j) { for (PartidaListener l : listeners) l.alAturdirse(j); }
    @Override 
    public void alFinalizar(ResultadoPartida r) { for (PartidaListener l : listeners) l.alFinalizar(r); }
}

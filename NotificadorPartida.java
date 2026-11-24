package dominio;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Clase responsable de notificar lo que pasa en la partida
 */
public class NotificadorPartida implements PartidaListener {
    private final List<PartidaListener> listeners = new CopyOnWriteArrayList<PartidaListener>();

    public void agregar(PartidaListener l) { listeners.add(l); }
    public void quitar(PartidaListener l) { listeners.remove(l); }

    @Override public void alCambiarEstado(EstadoPartida n) { 
        for (PartidaListener l : listeners) l.alCambiarEstado(n); 
    }
    @Override public void alGol(Lado a, boolean autogol) { 
        for (PartidaListener l : listeners) l.alGol(a, autogol); 
    }
    @Override public void alPatada(Jugador j) { 
        for (PartidaListener l : listeners) l.alPatada(j); 
    }
    @Override public void alActivarPoder(Jugador j) { 
        for (PartidaListener l : listeners) l.alActivarPoder(j); 
    }
    @Override public void alLanzarPoder(Jugador j) { 
        for (PartidaListener l : listeners) l.alLanzarPoder(j); 
    }
    @Override public void alRecibirDanio(Jugador v, double d) { 
        for (PartidaListener l : listeners) l.alRecibirDanio(v, d); 
    }
    @Override public void alAplicarEfecto(Jugador v, Efecto e) { 
        for (PartidaListener l : listeners) l.alAplicarEfecto(v, e); 
    }
    @Override public void alAturdirse(Jugador j) { 
        for (PartidaListener l : listeners) l.alAturdirse(j); 
    }
    @Override public void alFinalizar(ResultadoPartida r) { 
        for (PartidaListener l : listeners) l.alFinalizar(r); 
    }
}

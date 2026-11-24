package dominio;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
/**
 * Clase que representa el controlador humano
 * la presentacion avisa las teclas que se presionan
 */
public class ControladorHumano implements Controlador {
    private final Set<Accion> presionadas = EnumSet.noneOf(Accion.class);
    private final Set<Accion> vista = Collections.unmodifiableSet(presionadas);

    public void presionar(Accion a) { 
        presionadas.add(a); 
    }
    public void soltar(Accion a){ 
        presionadas.remove(a); 
    }
    public void soltarTodas(){ 
        presionadas.clear(); 
    }

    @Override
    public Set<Accion> decidir(Jugador yo, Partida partida, double dt){ 
        return vista; 
    }
}

package presentacion;

import javax.swing.JComponent;

/**
 * interfaz para las pantallas que necesitamos
 */
public interface Pantalla {
    IdPantalla getId();
    JComponent getVista();
    
    void alMostrar();
    
    void alOcultar();
}

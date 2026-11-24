package presentacion;

import javax.swing.JComponent;

/** Una pantalla de la aplicación. VentanaPrincipal las muestra de a una con un CardLayout. */
public interface Pantalla {
    IdPantalla getId();
    JComponent getVista();
    /** Se llama cada vez que la pantalla pasa a ser visible (aquí se cargan los datos). */
    void alMostrar();
    /** Se llama cuando la pantalla deja de ser visible (aquí se detienen temporizadores, teclado...). */
    void alOcultar();
}

package presentacion;

import javax.swing.JFrame;

/**
 * Lo único que las pantallas conocen de la ventana (principio de inversión de dependencias):
 * así una pantalla se puede probar o reemplazar sin depender de VentanaPrincipal.
 */
public interface Navegador {
    void ir(IdPantalla destino);
    ContextoJuego getContexto();
    JFrame getVentana();
    /** Cambia el tamaño del contenido de la ventana (se limita al tamaño de la pantalla). */
    void ajustarTamano(int ancho, int alto);
}

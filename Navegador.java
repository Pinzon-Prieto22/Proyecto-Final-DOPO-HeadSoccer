package presentacion;

import javax.swing.JFrame;

public interface Navegador {
    void ir(IdPantalla destino);
    ContextoJuego getContexto();
    JFrame getVentana();
    
    void ajustarTamano(int ancho, int alto);
}

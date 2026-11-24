package presentacion;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import dominio.Bitacora;
import dominio.DOPOSoccerException;

/** Punto de entrada de DOPOSoccer */
public class Main {
    public static void main(String[] args) {
        // Cualquier error que no se haya previsto queda en la bitácora y se le explica al usuario.
        Thread.setDefaultUncaughtExceptionHandler((hilo, error) -> {
            Bitacora.error("Excepción no controlada en el hilo " + hilo.getName(), error);
            SwingUtilities.invokeLater(() -> Mensajes.inesperado(null, error));
        });
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                } catch (Exception e) {
                    Bitacora.error("No se pudo aplicar el aspecto visual", e);
                }
                ContextoJuego contexto = new ContextoJuego();
                try {
                    contexto.getRegistro().cargar();
                } catch (DOPOSoccerException e) {
                    Mensajes.error(null, "No se pudieron cargar los perfiles", e);
                }
                new VentanaPrincipal(contexto).iniciar();
            }
        });
    }
}

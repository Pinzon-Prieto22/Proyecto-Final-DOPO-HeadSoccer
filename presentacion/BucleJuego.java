package presentacion;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import dominio.Partida;
import dominio.Reglas;

/**
 * Bucle del juego sobre javax.swing.Timer (corre en el hilo de Swing, sin problemas de concurrencia).
 * Usa paso de física FIJO con acumulador: el juego va igual de rápido en cualquier computador.
 */
public class BucleJuego implements ActionListener {
    private final Timer timer = new Timer(16, this);
    private final Partida partida;
    private final Runnable alCuadro;
    private long ultimo;
    private double acumulado;

    public BucleJuego(Partida partida, Runnable alCuadro) {
        this.partida = partida;
        this.alCuadro = alCuadro;
        timer.setCoalesce(true);
    }

    public void iniciar() {
        ultimo = System.nanoTime();
        acumulado = 0;
        timer.start();
    }

    public void detener() { timer.stop(); }

    @Override
    public void actionPerformed(ActionEvent e) {
        long ahora = System.nanoTime();
        double dt = Math.min(0.1, (ahora - ultimo) / 1e9);
        ultimo = ahora;
        acumulado += dt;
        while (acumulado >= Reglas.PASO_FISICA) {
            partida.actualizar(Reglas.PASO_FISICA);
            acumulado -= Reglas.PASO_FISICA;
        }
        alCuadro.run();
        Toolkit.getDefaultToolkit().sync();
    }
}

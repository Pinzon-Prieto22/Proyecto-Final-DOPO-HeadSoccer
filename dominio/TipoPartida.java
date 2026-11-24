package dominio;

import java.io.Serializable;

/**
 * Interfaz Tipo partida
 * Determina lo que debe definir cada tipo de partida segun sea su caso
 * 
 * es Serializable para poder guardarlo y abrirlo
 * 
 * @author Bryan Pinzon y Valentina Prieto
 * @version Entrega Final Proyecto Final
 */

public interface TipoPartida extends Serializable {
    String getNombre();
    void iniciar(Partida partida);
    void alPasarElTiempo(Partida partida, double dt);
    void alAnotarse(Partida partida, Lado anotador);
    boolean haTerminado();
    Lado getGanador();
    double getTiempoRestante();//en segundos
    boolean enMuerteSubita();
}

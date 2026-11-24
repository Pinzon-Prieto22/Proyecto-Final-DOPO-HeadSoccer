package dominio;

/**
 * interfaz para los tipos de partidas
 */
public interface TipoPartida {
    String getNombre();
    void iniciar(Partida partida);
    void alPasarElTiempo(Partida partida, double dt);
    void alAnotarse(Partida partida, Lado anotador);
    boolean haTerminado();
    
    Lado getGanador();
    
    double getTiempoRestante();
    boolean enMuerteSubita();
}

package dominio;

/**
 * Interfaz para los efectos
 */
public interface Efecto {
    String getId();
    String getNombre();
    void actualizar(double dt);
    boolean haTerminado();
    double getTiempoRestante();
    double getDuracion();

    boolean bloqueaAcciones();
    boolean bloqueaSalto();
    double factorVelocidad();

    default void alAplicar(Partida partida, Jugador victima) { }

    default void enCadaPaso(Jugador victima, double dt) { }
    
    default void alTerminar(Partida partida, Jugador victima) { }
}

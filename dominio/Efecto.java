package dominio;

import java.io.Serializable;

/**
 * Efecto de un tiro de poder sobre el rival. Los modificadores permiten que un efecto cambie el comportamiento
 * del jugador sin que Jugador conozca los efectos concretos, y los "ganchos" (alAplicar, enCadaPaso, alTerminar)
 * permiten efectos especiales como el empuje o la ilusión. Para un efecto nuevo basta con extender EfectoTemporal.
 */
public interface Efecto extends Serializable {
    String getId();
    String getNombre();
    void actualizar(double dt);
    boolean haTerminado();
    double getTiempoRestante();
    double getDuracion();

    /** Si es true el jugador no puede moverse, saltar ni patear. */
    boolean bloqueaAcciones();
    boolean bloqueaSalto();
    double factorVelocidad();

    /** Se llama una vez cuando el efecto se aplica a la víctima. */
    default void alAplicar(Partida partida, Jugador victima) { }
    /** Se llama en cada paso de física mientras el efecto está activo. */
    default void enCadaPaso(Jugador victima, double dt) { }
    /** Se llama cuando el efecto termina (por tiempo, por ser reemplazado o al reiniciar la ronda). */
    default void alTerminar(Partida partida, Jugador victima) { }
}

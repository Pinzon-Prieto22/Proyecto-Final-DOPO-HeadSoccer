package dominio;

/**
 * Interfaz PartidaListener
 * Responsable de "observar" la patida. Es para avisarle a la interfaz las cosas que pasan
 * 
 * @author Bryan Pinzon y Valentina Prieto
 * @version Entrega Final Proyecto Final
 */

public interface PartidaListener {
    default void alCambiarEstado(EstadoPartida nuevo) { }
    default void alGol(Lado anotador, boolean autogol) { }
    default void alPatada(Jugador jugador) { }
    default void alActivarPoder(Jugador jugador) { }
    default void alLanzarPoder(Jugador jugador) { }
    default void alRecibirDanio(Jugador victima, double danio) { }
    default void alAplicarEfecto(Jugador victima, Efecto efecto) { }
    default void alAturdirse(Jugador jugador) { }
    default void alFinalizar(ResultadoPartida resultado) { }
}

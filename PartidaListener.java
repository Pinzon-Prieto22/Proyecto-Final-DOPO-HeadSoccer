package dominio;

/**
 * Interfaz para el observador de la partida 
 * es como el dominio le va avisando a la parte de presentacion
 * 
 * todos los metodos son opcionales
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

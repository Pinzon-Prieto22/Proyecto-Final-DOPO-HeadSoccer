package dominio;

/**
 * Clase responsable de armar la partida segun la configuracion
 */
public final class FabricaPartida {
    private FabricaPartida() { }

    public static Partida crear(ConfiguracionJuego c) throws DOPOSoccerException {
        c.validar();
        TipoPartida tipo = c.crearTipoPartida();
        return new Partida(crearJugador(c, Lado.IZQUIERDO), crearJugador(c, Lado.DERECHO), tipo);
    }

    private static Jugador crearJugador(ConfiguracionJuego c, Lado lado) {
        String nombre = lado == Lado.IZQUIERDO ? "Jugador 1" : "Jugador 2";
        return new Jugador(lado, c.getPersonaje(lado), nombre, new Estadisticas(), new ControladorHumano());
    }
}

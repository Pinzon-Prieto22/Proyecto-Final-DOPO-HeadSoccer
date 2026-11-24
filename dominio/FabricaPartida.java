package dominio;

/**
 * Clase FabricaPartida
 * Responsable de armar la partida a partir de la configuracion
 * 
 * @author Bryan Pinzon y Valentina Prieto
 * @version Entrega Final Proyecto Final
 */

public final class FabricaPartida {
    private FabricaPartida() { }

    /**
     * MEtodo que crea la partida
     * @param c que es la configuracion dada
     * @return
     * @throws DOPOSoccerException
     */
    public static Partida crear(ConfiguracionJuego c) throws DOPOSoccerException {
        c.validar();
        TipoPartida tipo = c.crearTipoPartida();
        return new Partida(crearJugador(c, Lado.IZQUIERDO), crearJugador(c, Lado.DERECHO), tipo);
    }

    /**
     *  metodo que crea un jugador
     * @param c la configuracion dada
     * @param lado lado donde se quiere poner
     * @return
     */
    private static Jugador crearJugador(ConfiguracionJuego c, Lado lado) {
        Personaje personaje = c.getPersonaje(lado);
        // obtenemos la modalidad y si es humano el que esta en ese lado
        if (c.getModalidad().esHumano(lado)) {
            // obtenemos el perfil
            Perfil p = c.getPerfil(lado);
            // el partido trabaja con una copiade las estadisticas del perfil
            // creamos el jugador humano
            return new Jugador(lado, personaje, p.getNombre(), new Estadisticas(p.getEstadisticas()), p.getPuntos(), new ControladorHumano());
        }
        // obtenemos la maquina y creamos un jugador maquina
        FabricaMaquina m = c.getMaquina();
        return new Jugador(lado, personaje, "Maquina " + m.getNombre(), new Estadisticas(), 0, m.crear());
    }
}

package dominio;

import java.util.List;

/**
 * Prueba del dominio SIN interfaz gráfica: enfrenta a todas las combinaciones de personajes y perfiles de máquina
 * y muestra estadísticas de los partidos. Se ejecuta desde BlueJ (clic derecho > void main).
 */
public class SimulacionConsola {
    private static int poderes, efectos, patadas, aturdimientos, goles;

    public static void main(String[] args) throws DOPOSoccerException {
        List<Personaje> personajes = CatalogoPersonajes.getInstancia().getTodos();
        List<FabricaMaquina> maquinas = CatalogoMaquinas.getInstancia().getTodas();
        int partidos = 0, terminados = 0;
        for (Personaje pi : personajes) {
            for (Personaje pd : personajes) {
                for (FabricaMaquina mi : maquinas) {
                    for (FabricaMaquina md : maquinas) {
                        if (pi == pd && mi == md) continue;
                        Jugador ji = new Jugador(Lado.IZQUIERDO, pi, "A", new Estadisticas(), 0, mi.crear());
                        Jugador jd = new Jugador(Lado.DERECHO, pd, "B", new Estadisticas(), 0, md.crear());
                        Partida p = new Partida(ji, jd, new PartidaPorTiempo(1));
                        p.agregarListener(new PartidaListener() {
                            @Override public void alLanzarPoder(Jugador j) { poderes++; }
                            @Override public void alAplicarEfecto(Jugador v, Efecto e) { efectos++; }
                            @Override public void alPatada(Jugador j) { patadas++; }
                            @Override public void alAturdirse(Jugador j) { aturdimientos++; }
                            @Override public void alGol(Lado a, boolean auto) { goles++; }
                        });
                        partidos++;
                        for (double t = 0; t < 400 && p.getEstado() != EstadoPartida.FINALIZADA; t += Reglas.PASO_FISICA) p.actualizar(Reglas.PASO_FISICA);
                        if (p.getEstado() == EstadoPartida.FINALIZADA) terminados++;
                    }
                }
            }
        }
        System.out.println("Partidos simulados: " + partidos + " | terminados: " + terminados);
        System.out.println("Goles: " + goles + " | patadas: " + patadas + " | poderes lanzados: " + poderes
                + " | efectos/daños aplicados: " + efectos + " | aturdimientos: " + aturdimientos);
    }
}

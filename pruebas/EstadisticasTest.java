package pruebas;

import static org.junit.Assert.*;
import org.junit.Test;
import dominio.*;

/** Fórmulas del Anexo 2 del enunciado. */
public class EstadisticasTest {
    private static final double D = 1e-9;

    @Test
    public void todasEmpiezanEnUno() {
        Estadisticas e = new Estadisticas();
        for (TipoEstadistica t : TipoEstadistica.values()) assertEquals(1, e.getNivel(t));
        assertEquals(100, e.getVidaMaxima(), D);
        assertEquals(1.0, e.getMultiplicadorKick(), D);
        assertEquals(10, e.getDanioPatada(), D);
    }

    @Test
    public void vidaMaximaCreceDiezPorCientoPorNivel() throws Exception {
        Perfil p = perfilConPuntos(1000000);
        for (int nivel = 2; nivel <= 10; nivel++) {
            p.comprarMejora(TipoEstadistica.SURVIVAL);
            assertEquals(100 * (1 + 0.1 * (nivel - 1)), p.getEstadisticas().getVidaMaxima(), D);
        }
        assertEquals(190, p.getEstadisticas().getVidaMaxima(), D);   // tabla 2: Survival 10 = 190
    }

    @Test
    public void multiplicadorKickYDanioDePatada() throws Exception {
        Perfil p = perfilConPuntos(1000000);
        for (int i = 0; i < 4; i++) p.comprarMejora(TipoEstadistica.KICK);    // Kick 5
        assertEquals(1.4, p.getEstadisticas().getMultiplicadorKick(), 1e-9);
        assertEquals(14, p.getEstadisticas().getDanioPatada(), 1e-9);          // ejemplo del enunciado
    }

    @Test
    public void preciosSeDuplicanPorEstadistica() throws Exception {
        Perfil p = perfilConPuntos(1000000);
        assertEquals(500, p.getEstadisticas().getPrecioSiguiente(TipoEstadistica.SPEED));
        p.comprarMejora(TipoEstadistica.SPEED);
        assertEquals(1000, p.getEstadisticas().getPrecioSiguiente(TipoEstadistica.SPEED));
        p.comprarMejora(TipoEstadistica.SPEED);
        assertEquals(2000, p.getEstadisticas().getPrecioSiguiente(TipoEstadistica.SPEED));
        assertEquals("otra estadística vuelve a costar 500", 500, p.getEstadisticas().getPrecioSiguiente(TipoEstadistica.JUMP));
    }

    @Test
    public void velocidadYSaltoSubenDiezPorCientoDelBase() throws Exception {
        Perfil p = perfilConPuntos(1000000);
        p.comprarMejora(TipoEstadistica.SPEED);
        assertEquals(Reglas.VELOCIDAD_BASE * 1.1, p.getEstadisticas().getVelocidad(), D);
        double alturaBase = Math.pow(Reglas.IMPULSO_SALTO_BASE, 2) / (2 * Reglas.GRAVEDAD_JUGADOR);
        p.comprarMejora(TipoEstadistica.JUMP);
        double altura = Math.pow(p.getEstadisticas().getImpulsoSalto(), 2) / (2 * Reglas.GRAVEDAD_JUGADOR);
        assertEquals(alturaBase * 1.1, altura, 1e-6);
    }

    @Test
    public void noSePuedePasarDelNivelDiez() throws Exception {
        Perfil p = perfilConPuntos(10000000);
        for (int i = 0; i < 9; i++) p.comprarMejora(TipoEstadistica.KICK);
        assertEquals(-1, p.getEstadisticas().getPrecioSiguiente(TipoEstadistica.KICK));
        try { p.comprarMejora(TipoEstadistica.KICK); fail("debía lanzar excepción"); }
        catch (DOPOSoccerException e) { assertTrue(e.getMessage().contains("máximo")); }
    }

    /** Un perfil con puntos (se gana jugando: aquí se simula con partidas ganadas). */
    static Perfil perfilConPuntos(int puntos) throws DOPOSoccerException {
        Perfil p = new Perfil("Prueba");
        p.registrarPartida(true, puntos);
        return p;
    }
}

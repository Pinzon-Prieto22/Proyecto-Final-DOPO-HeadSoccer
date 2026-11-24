package dominio;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Clase responsable de las pruebas a las estadisticas
 */
public class EstadisticasTest {
    private static final double D = 1e-9;

    private static Estadisticas con(TipoEstadistica t, int nivel) {
        Estadisticas e = new Estadisticas();
        for (int i = 1; i < nivel; i++) e.subir(t);
        return e;
    }

    @Test
    public void todasEmpiezanEnUno() {
        Estadisticas e = new Estadisticas();
        for (TipoEstadistica t : TipoEstadistica.values()) assertEquals(1, e.getNivel(t));
        assertEquals(100, e.getVidaMaxima(), D);
        assertEquals(1.0, e.getMultiplicadorKick(), D);
        assertEquals(10, e.getDanioPatada(), D);
    }

    @Test
    public void vidaMaximaCreceDiezPorCientoPorNivel() {
        for (int nivel = 1; nivel <= 10; nivel++) {
            assertEquals(100 * (1 + 0.1 * (nivel - 1)), con(TipoEstadistica.SURVIVAL, nivel).getVidaMaxima(), D);
        }
        assertEquals(190, con(TipoEstadistica.SURVIVAL, 10).getVidaMaxima(), D);   // tabla 2: Survival 10 = 190
    }

    @Test
    public void multiplicadorKickYDanioDePatada() {
        Estadisticas e = con(TipoEstadistica.KICK, 5);
        assertEquals(1.4, e.getMultiplicadorKick(), D);
        assertEquals(14, e.getDanioPatada(), D);                   // ejemplo del enunciado
        assertEquals(1.9, con(TipoEstadistica.KICK, 10).getMultiplicadorKick(), D);   // tabla 3
        assertEquals(19, con(TipoEstadistica.KICK, 10).getDanioPatada(), D);
    }

    @Test
    public void velocidadYSaltoSubenDiezPorCientoDelBase() {
        assertEquals(Reglas.VELOCIDAD_BASE * 1.1, con(TipoEstadistica.SPEED, 2).getVelocidad(), D);
        double alturaBase = Math.pow(Reglas.IMPULSO_SALTO_BASE, 2) / (2 * Reglas.GRAVEDAD_JUGADOR);
        double altura = Math.pow(con(TipoEstadistica.JUMP, 2).getImpulsoSalto(), 2) / (2 * Reglas.GRAVEDAD_JUGADOR);
        assertEquals(alturaBase * 1.1, altura, 1e-6);
    }

    @Test
    public void unaCopiaNoComparteNivelesConLaOriginal() {
        Estadisticas original = new Estadisticas();
        Estadisticas copia = new Estadisticas(original);
        copia.subir(TipoEstadistica.SPEED);
        assertEquals(1, original.getNivel(TipoEstadistica.SPEED));
        assertEquals(2, copia.getNivel(TipoEstadistica.SPEED));
    }

    @Test
    public void noSePuedePasarDelNivelDiez() {
        Estadisticas e = con(TipoEstadistica.KICK, 10);
        assertTrue(e.estaEnMaximo(TipoEstadistica.KICK));
        try { e.subir(TipoEstadistica.KICK); fail("debía lanzar excepción"); }
        catch (IllegalStateException ex) { assertTrue(ex.getMessage().contains("máximo")); }
    }
}

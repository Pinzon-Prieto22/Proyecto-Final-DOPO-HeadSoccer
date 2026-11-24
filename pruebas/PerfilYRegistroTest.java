package pruebas;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.Test;
import dominio.*;

public class PerfilYRegistroTest {
    /** Repositorio en memoria: las pruebas no tocan el disco. */
    static class Memoria implements RepositorioPerfiles {
        List<Perfil> guardados = new ArrayList<Perfil>();
        int guardadas;
        public List<Perfil> cargar() { return new ArrayList<Perfil>(guardados); }
        public void guardar(Collection<Perfil> p) { guardados = new ArrayList<Perfil>(p); guardadas++; }
    }

    @Test
    public void mejoraDescuentaPuntos() throws Exception {
        Perfil p = EstadisticasTest.perfilConPuntos(1500);
        p.comprarMejora(TipoEstadistica.SPEED);      // 500
        p.comprarMejora(TipoEstadistica.SPEED);      // 1000
        assertEquals(0, p.getPuntos());
        assertEquals(3, p.getEstadisticas().getNivel(TipoEstadistica.SPEED));
    }

    @Test
    public void sinPuntosSuficientesLanzaExcepcionClara() throws Exception {
        Perfil p = EstadisticasTest.perfilConPuntos(499);
        try { p.comprarMejora(TipoEstadistica.JUMP); fail(); }
        catch (DOPOSoccerException e) {
            assertTrue(e.getMessage().contains("500"));
            assertNotNull(e.getSugerencia());
        }
        assertEquals(499, p.getPuntos());
        assertEquals(1, p.getEstadisticas().getNivel(TipoEstadistica.JUMP));
    }

    @Test
    public void nombresInvalidosSeRechazan() {
        for (String malo : new String[] { "", "   ", null, "nombre-demasiado-largo-123", "a/b", "x;y" }) {
            try { new Perfil(malo); fail("debía rechazar: " + malo); } catch (DOPOSoccerException e) { /* ok */ }
        }
    }

    @Test
    public void registroNoPermiteDuplicadosSinImportarMayusculas() throws Exception {
        RegistroPerfiles r = new RegistroPerfiles(new Memoria());
        r.crear("Ana");
        try { r.crear("ANA"); fail(); } catch (DOPOSoccerException e) { assertTrue(e.getMessage().contains("Ya existe")); }
        assertEquals(1, r.getTodos().size());
        assertNotNull(r.buscar("ana"));
    }

    @Test
    public void elProgresoSeGuardaYSeRecupera() throws Exception {
        Memoria m = new Memoria();
        RegistroPerfiles r = new RegistroPerfiles(m);
        Perfil ana = r.crear("Ana");
        ana.registrarPartida(true, 600);
        r.mejorar(ana, TipoEstadistica.KICK);
        RegistroPerfiles otro = new RegistroPerfiles(m);
        otro.cargar();
        assertEquals(100, otro.buscar("Ana").getPuntos());
        assertEquals(2, otro.buscar("Ana").getEstadisticas().getNivel(TipoEstadistica.KICK));
    }

    @Test
    public void puntosGanadorCienPerdedorVeinticinco() throws Exception {
        RegistroPerfiles r = new RegistroPerfiles(new Memoria());
        r.crear("Ana"); r.crear("Beto");
        Partida p = PartidaTest.partidaPvP(r.buscar("Ana"), r.buscar("Beto"), new PartidaPorGoles(1));
        PartidaTest.meterGol(p, Lado.DERECHO);       // gol de Ana (izquierda) en el arco derecho
        PartidaTest.correr(p, 10);
        ResultadoPartida res = p.getResultado();
        assertEquals(Lado.IZQUIERDO, res.getGanador());
        r.registrarResultado(res);
        assertEquals(100, r.buscar("Ana").getPuntos());
        assertEquals(25, r.buscar("Beto").getPuntos());
    }

    @Test
    public void partidaTerminadaManualmenteNoDaPuntos() throws Exception {
        RegistroPerfiles r = new RegistroPerfiles(new Memoria());
        r.crear("Ana"); r.crear("Beto");
        Partida p = PartidaTest.partidaPvP(r.buscar("Ana"), r.buscar("Beto"), new PartidaPorGoles(5));
        p.terminarManualmente();
        ResultadoPartida res = p.getResultado();
        assertNull(res.getGanador());
        assertTrue(res.fueTerminadaPorUsuario());
        r.registrarResultado(res);
        assertEquals(0, r.buscar("Ana").getPuntos());
        assertEquals(0, r.buscar("Beto").getPuntos());
    }
}

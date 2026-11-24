package dominio;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Clase responsable de las pruebas
 */
public class PartidaTest {
    // utilidades
    static Estadisticas estadisticas(int kick, int survival) {
        Estadisticas e = new Estadisticas();
        for (int i = 1; i < kick; i++) e.subir(TipoEstadistica.KICK);
        for (int i = 1; i < survival; i++) e.subir(TipoEstadistica.SURVIVAL);
        return e;
    }

    static Partida partidaPvP(Estadisticas izq, Estadisticas der, TipoPartida tipo) {
        Personaje a = CatalogoPersonajes.getInstancia().buscar("cmr");
        Personaje b = CatalogoPersonajes.getInstancia().buscar("kor");
        return new Partida(
            new Jugador(Lado.IZQUIERDO, a, "Jugador 1", izq, new ControladorHumano()),
            new Jugador(Lado.DERECHO, b, "Jugador 2", der, new ControladorHumano()),
            tipo);
    }

    static Partida partida(String idIzq, String idDer, TipoPartida tipo) throws Exception {
        CatalogoPersonajes c = CatalogoPersonajes.getInstancia();
        return new Partida(
            new Jugador(Lado.IZQUIERDO, c.buscar(idIzq), "A", new Estadisticas(), new ControladorHumano()),
            new Jugador(Lado.DERECHO, c.buscar(idDer), "B", new Estadisticas(), new ControladorHumano()),
            tipo);
    }

    static void correr(Partida p, double segundos) {
        for (double t = 0; t < segundos; t += Reglas.PASO_FISICA) p.actualizar(Reglas.PASO_FISICA);
    }

    static void empezar(Partida p) { correr(p, Reglas.DURACION_CUENTA_REGRESIVA + 0.1); }

    static void meterGol(Partida p, Lado arco) {
        empezar(p);
        double x = arco == Lado.IZQUIERDO ? 40 : Reglas.ANCHO - 40;
        p.getBalon().setPosicion(x, Reglas.Y_SUELO - 25);
        p.getBalon().setVelocidad(0, 0);
        p.actualizar(Reglas.PASO_FISICA);
    }

    // inicio
    @Test
    public void iniciaEnCeroConBarrasVaciasYVidaMaxima() throws Exception {
        Partida p = partida("cmr", "kor", new PartidaPorGoles(3));
        assertEquals(0, p.getMarcador().getGoles(Lado.IZQUIERDO));
        assertEquals(0, p.getMarcador().getGoles(Lado.DERECHO));
        for (Lado l : Lado.values()) {
            assertEquals(0, p.getJugador(l).getMedidor().getPorcentaje(), 1e-9);
            assertEquals(p.getJugador(l).getVidaMaxima(), p.getJugador(l).getVida(), 1e-9);
            assertFalse(p.getJugador(l).tieneAura());
        }
    }

    // goles y tipos de partido
    @Test
    public void golEnArcoContrarioPuntuaAlJugadorYAutogolAlRival() throws Exception {
        Partida p = partida("cmr", "kor", new PartidaPorGoles(5));
        meterGol(p, Lado.DERECHO);                      // entra en el arco derecho -> punto del izquierdo
        assertEquals(1, p.getMarcador().getGoles(Lado.IZQUIERDO));
        Partida q = partida("cmr", "kor", new PartidaPorGoles(5));
        empezar(q);
        q.getBalon().setUltimoToque(q.getJugador(Lado.IZQUIERDO));   // el izquierdo toca y mete en SU arco
        q.getBalon().setPosicion(40, Reglas.Y_SUELO - 25);
        q.actualizar(Reglas.PASO_FISICA);
        assertEquals(1, q.getMarcador().getGoles(Lado.DERECHO));
        assertTrue(q.isUltimoAutogol());
    }

    @Test
    public void porGolesGanaElPrimeroEnLlegarSinLimiteDeTiempo() throws Exception {
        Partida p = partida("cmr", "kor", new PartidaPorGoles(2));
        meterGol(p, Lado.DERECHO); correr(p, 3);
        assertNotEquals(EstadoPartida.FINALIZADA, p.getEstado());
        meterGol(p, Lado.DERECHO); correr(p, 5);
        assertEquals(EstadoPartida.FINALIZADA, p.getEstado());
        assertEquals(Lado.IZQUIERDO, p.getResultado().getGanador());
    }

    @Test
    public void porTiempoGanaQuienTieneMasGoles() throws Exception {
        Partida p = partida("cmr", "kor", new PartidaPorTiempo(1));
        meterGol(p, Lado.IZQUIERDO);                    // gol del derecho
        correr(p, 70);
        assertEquals(EstadoPartida.FINALIZADA, p.getEstado());
        assertEquals(Lado.DERECHO, p.getResultado().getGanador());
    }

    @Test
    public void conEmpateHayMuerteSubitaYGanaElPrimeroEnMarcar() throws Exception {
        Partida p = partida("cmr", "kor", new PartidaPorTiempo(1));
        empezar(p);
        correr(p, 62);                                  // 0-0 al terminar el tiempo
        assertTrue(p.getTipo().enMuerteSubita());
        assertNotEquals(EstadoPartida.FINALIZADA, p.getEstado());
        p.getBalon().setPosicion(Reglas.ANCHO - 40, Reglas.Y_SUELO - 25);
        p.getBalon().setVelocidad(0, 0);
        correr(p, 6);
        assertEquals(EstadoPartida.FINALIZADA, p.getEstado());
        assertEquals(Lado.IZQUIERDO, p.getResultado().getGanador());
        assertTrue(p.getResultado().huboMuerteSubita());
    }

    // barra de poder
    @Test
    public void barraSeLlenaEnDiezSegundosYRecibirGolSubeUnCuarto() throws Exception {
        Partida p = partida("cmr", "kor", new PartidaPorGoles(9));
        empezar(p);
        correr(p, 5);
        assertEquals(50, p.getJugador(Lado.IZQUIERDO).getMedidor().getPorcentaje(), 2.0);
        correr(p, 5.5);
        assertTrue(p.getJugador(Lado.IZQUIERDO).getMedidor().estaLleno());
        Partida q = partida("cmr", "kor", new PartidaPorGoles(9));
        meterGol(q, Lado.IZQUIERDO);                    // gol que recibe el izquierdo
        assertEquals(25, q.getJugador(Lado.IZQUIERDO).getMedidor().getPorcentaje(), 2.0);
        assertEquals(0, q.getJugador(Lado.DERECHO).getMedidor().getPorcentaje(), 2.0);
    }

    @Test
    public void activarPoderVaciaLaBarraDaAuraYNoSePuedeActivarOtro() throws Exception {
        Partida p = partida("cmr", "kor", new PartidaPorGoles(9));
        empezar(p);
        Jugador j = p.getJugador(Lado.IZQUIERDO);
        j.activarPoder();
        assertFalse("con la barra vacía no hay aura", j.tieneAura());
        correr(p, 10.5);
        j.activarPoder();
        assertTrue(j.tieneAura());
        assertTrue("la barra se vacía y empieza a recargarse", j.getMedidor().getPorcentaje() < 10);
        correr(p, 10.5);                                // vuelve a llenarse mientras tiene el aura
        assertTrue(j.getMedidor().estaLleno());
        j.activarPoder();
        assertEquals("sigue llena: no pudo activar otro poder", 100, j.getMedidor().getPorcentaje(), 1e-9);
    }

    // daño, vida y aturdimiento
    @Test
    public void ejemploDelEnunciadoPatadaYTiroDePoderDeCamerun() throws Exception {
        Estadisticas j1 = estadisticas(5, 1);             // Kick 5
        Estadisticas j2 = estadisticas(1, 3);             // Survival 3
        Partida p = partidaPvP(j1, j2, new PartidaPorGoles(9));
        Jugador a = p.getJugador(Lado.IZQUIERDO), b = p.getJugador(Lado.DERECHO);
        assertEquals(120, b.getVidaMaxima(), 1e-9);
        b.recibirDanio(a.getEstadisticas().getDanioPatada());
        assertEquals(106, b.getVida(), 1e-9);
        b.recibirDanio(0);                              // sin cambios
        Partida q = partidaPvP(estadisticas(5, 1), estadisticas(1, 3), new PartidaPorGoles(9));
        Jugador a2 = q.getJugador(Lado.IZQUIERDO), b2 = q.getJugador(Lado.DERECHO);
        Efecto e = a2.getPersonaje().getPoder().aplicarA(b2, a2, q);   // Rayo de Camerún: 60 x 1,4 = 84
        assertEquals(36, b2.getVida(), 1e-9);
        assertEquals("electrocucion", e.getId());
        assertEquals(0.5, b2.getVelocidadActual() / b2.getEstadisticas().getVelocidad(), 1e-9);
    }

    @Test
    public void vidaEnCeroAturde3SegundosYLuegoRecuperaLaVidaMaxima() throws Exception {
        Partida p = partida("cmr", "kor", new PartidaPorGoles(9));
        empezar(p);
        Jugador b = p.getJugador(Lado.DERECHO);
        b.recibirDanio(1000);
        assertEquals(0, b.getVida(), 1e-9);
        assertTrue(b.estaAturdido());
        assertTrue(b.estaBloqueado());
        correr(p, 2.5);
        assertTrue("sigue aturdido a los 2,5 s", b.estaAturdido());
        correr(p, 0.6);
        assertFalse(b.estaAturdido());
        assertEquals(b.getVidaMaxima(), b.getVida(), 1e-9);
    }

    @Test
    public void laVidaNuncaEsNegativa() throws Exception {
        Partida p = partida("cmr", "kor", new PartidaPorGoles(9));
        p.getJugador(Lado.DERECHO).recibirDanio(5000);
        assertEquals(0, p.getJugador(Lado.DERECHO).getVida(), 1e-9);
    }

    // poderes
    @Test
    public void danioRealDeCadaPoderSegunElAnexo() throws Exception {
        CatalogoPersonajes c = CatalogoPersonajes.getInstancia();
        Partida p = partida("cmr", "kor", new PartidaPorGoles(9));
        Jugador autor = p.getJugador(Lado.IZQUIERDO);       // Kick 1: multiplicador 1
        assertEquals(40, c.buscar("kor").getPoder().calcularDanio(autor), 1e-9);
        assertEquals(60, c.buscar("cmr").getPoder().calcularDanio(autor), 1e-9);
        assertEquals(40, c.buscar("nga").getPoder().calcularDanio(autor), 1e-9);
        assertEquals(30, c.buscar("usa").getPoder().calcularDanio(autor), 1e-9);
    }

    @Test
    public void elEfectoNuevoReemplazaAlAnteriorYNoSeAcumulan() throws Exception {
        Partida p = partida("cmr", "kor", new PartidaPorGoles(9));
        Jugador autor = p.getJugador(Lado.IZQUIERDO), victima = p.getJugador(Lado.DERECHO);
        new PoderRayo().aplicarA(victima, autor, p);
        assertEquals("electrocucion", victima.getEfecto().getId());
        new PoderTornado().aplicarA(victima, autor, p);
        assertEquals("inconsciente", victima.getEfecto().getId());
        assertEquals(1.0, victima.getVelocidadActual() / victima.getEstadisticas().getVelocidad(), 1e-9);   // ya no va al 50 %
    }

    @Test
    public void tornadoDejaInconscienteNoPuedeMoverseNiSaltarNiPatear() throws Exception {
        Partida p = partida("nga", "kor", new PartidaPorGoles(9));
        empezar(p);
        Jugador victima = p.getJugador(Lado.DERECHO);
        new PoderTornado().aplicarA(victima, p.getJugador(Lado.IZQUIERDO), p);
        assertTrue(victima.estaBloqueado());
        assertTrue("es lanzado al aire", victima.getVy() < 0);
        correr(p, 3.2);
        assertFalse(victima.estaBloqueado());
    }

    @Test
    public void auraAzulEmpujaAlRivalHaciaSuPropioArco() throws Exception {
        Partida p = partida("kor", "cmr", new PartidaPorGoles(9));
        empezar(p);
        Jugador autor = p.getJugador(Lado.IZQUIERDO), victima = p.getJugador(Lado.DERECHO);
        double x0 = victima.getX();
        autor.getPersonaje().getPoder().aplicarA(victima, autor, p);
        correr(p, 0.5);
        assertTrue("el derecho es empujado hacia su arco (a la derecha)", victima.getX() > x0 + 30);
    }

    @Test
    public void ilusionCreaSieteBalonesFalsosYDesaparecenAlTerminar() throws Exception {
        Partida p = partida("usa", "kor", new PartidaPorGoles(9));
        empezar(p);
        Jugador autor = p.getJugador(Lado.IZQUIERDO), victima = p.getJugador(Lado.DERECHO);
        new PoderIlusion().aplicarA(victima, autor, p);
        assertEquals(7, p.getBalonesFalsos().size());
        correr(p, 1.0);
        assertEquals(7, p.getBalonesFalsos().size());
        correr(p, 1.7);
        assertEquals(0, p.getBalonesFalsos().size());
    }

    @Test
    public void tiroDePoderQueEntraAlArcoSinTocarAlRivalCuentaGolSinEfecto() throws Exception {
        Partida p = partida("cmr", "kor", new PartidaPorGoles(9));
        empezar(p);
        Jugador autor = p.getJugador(Lado.IZQUIERDO), rival = p.getJugador(Lado.DERECHO);
        rival.setPosicion(300, Reglas.Y_SUELO);                      // el rival se aparta del camino
        p.getBalon().setPosicion(Reglas.ANCHO - 120, Reglas.Y_SUELO - 60);
        p.getBalon().setVelocidad(0, 0);
        autor.getPersonaje().getPoder().lanzar(p.getBalon(), autor);
        p.getBalon().setPosicion(Reglas.ANCHO - 200, Reglas.Y_SUELO - 40);
        correr(p, 1.0);
        assertEquals(1, p.getMarcador().getGoles(Lado.IZQUIERDO));
        assertEquals(rival.getVidaMaxima(), rival.getVida(), 1e-9);
        assertNull(rival.getEfecto());
    }

    // configuración
    @Test
    public void laFabricaCreaUnPartidoJugadorContraJugadorConLosPersonajesElegidos() throws Exception {
        ConfiguracionJuego c = new ConfiguracionJuego();
        CatalogoPersonajes cat = CatalogoPersonajes.getInstancia();
        c.setModalidad(Modalidad.PVP);
        c.setPersonaje(Lado.IZQUIERDO, cat.buscar("usa"));
        c.setPersonaje(Lado.DERECHO, cat.buscar("nga"));
        Partida p = FabricaPartida.crear(c);
        assertEquals("usa", p.getJugador(Lado.IZQUIERDO).getPersonaje().getId());
        assertEquals("nga", p.getJugador(Lado.DERECHO).getPersonaje().getId());
        assertTrue(p.getJugador(Lado.IZQUIERDO).esHumano());
        assertTrue(p.getJugador(Lado.DERECHO).esHumano());
        assertEquals(EstadoPartida.CUENTA_REGRESIVA, p.getEstado());
    }

    @Test
    public void configuracionSinPersonajeEsRechazadaConMensajeClaro() {
        ConfiguracionJuego c = new ConfiguracionJuego();
        c.setPersonaje(Lado.DERECHO, null);
        try { FabricaPartida.crear(c); fail(); }
        catch (DOPOSoccerException e) {
            assertTrue(e.getMessage().contains("personaje del jugador 2"));
            assertNotNull(e.getSugerencia());
        }
    }

    // pausa y fin manual
    @Test
    public void pausarCongelaElPartidoYReanudarLoContinua() throws Exception {
        Partida p = partida("cmr", "kor", new PartidaPorTiempo(2));
        empezar(p);
        p.pausar();
        double restante = p.getTipo().getTiempoRestante();
        correr(p, 5);
        assertEquals(EstadoPartida.PAUSADA, p.getEstado());
        assertEquals(restante, p.getTipo().getTiempoRestante(), 1e-9);
        p.reanudar();
        correr(p, 1);
        assertEquals(EstadoPartida.JUGANDO, p.getEstado());
        assertTrue(p.getTipo().getTiempoRestante() < restante);
    }

    @Test
    public void terminarManualmenteFinalizaSinGanador() throws Exception {
        Partida p = partida("cmr", "kor", new PartidaPorGoles(5));
        meterGol(p, Lado.DERECHO);
        p.terminarManualmente();
        assertEquals(EstadoPartida.FINALIZADA, p.getEstado());
        assertNull(p.getResultado().getGanador());
        assertTrue(p.getResultado().fueTerminadaPorUsuario());
    }

    @Test
    public void tiposDePartidaValidanSuValor() {
        try { new FabricaPorGoles().crear(0); fail(); } catch (DOPOSoccerException e) { /* ok */ }
        try { new FabricaPorTiempo().crear(99); fail(); } catch (DOPOSoccerException e) { /* ok */ }
    }
}

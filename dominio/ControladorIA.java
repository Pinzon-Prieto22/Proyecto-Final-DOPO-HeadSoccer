package dominio;

import java.util.EnumSet;
import java.util.Random;
import java.util.Set;

/**
 * Base de las máquinas se encarga del tiempo de reacción y del error al apuntar;
 * cada perfil de máquina solo define su estrategia en elegirAcciones(). Incluye utilidades de movimiento comunes.
 */
public abstract class ControladorIA implements Controlador {
    private static final long serialVersionUID = 1L;
    protected final Random azar = new Random();
    private final double tiempoReaccion;
    private final double margenError;
    private final EnumSet<Accion> acciones = EnumSet.noneOf(Accion.class);
    private double acumulado = 1e9;
    private double error;

    protected ControladorIA(double tiempoReaccion, double margenError) {
        this.tiempoReaccion = tiempoReaccion;
        this.margenError = margenError;
    }

    @Override
    public final Set<Accion> decidir(Jugador yo, Partida partida, double dt) {
        acumulado += dt;
        if (acumulado >= tiempoReaccion) {
            acumulado = 0;
            error = (azar.nextDouble() * 2 - 1) * margenError;
            acciones.clear();
            elegirAcciones(yo, partida, acciones);
        }
        return acciones;
    }

    /** Estrategia del perfil: decide las acciones para el próximo intervalo de reacción. */
    protected abstract void elegirAcciones(Jugador yo, Partida partida, EnumSet<Accion> acciones);

    protected Jugador rival(Jugador yo, Partida p) { return p.getJugador(yo.getLado().opuesto()); }

    protected void irHacia(Jugador yo, double objetivoX, EnumSet<Accion> a) {
        double dx = objetivoX + error - yo.getX();
        double tolerancia = Math.max(8, yo.getVelocidadActual() * tiempoReaccion * 0.5);
        if (dx > tolerancia) a.add(Accion.DERECHA);
        else if (dx < -tolerancia) a.add(Accion.IZQUIERDA);
    }

    /** Persigue el balón colocándose detrás de él, patea cuando lo tiene delante y salta si viene alto. */
    protected void atacarBalon(Jugador yo, Balon b, EnumSet<Accion> a) {
        int dir = yo.getDireccion();
        irHacia(yo, b.getX() + b.getVx() * 0.12 - dir * 52, a);
        double delante = (b.getX() - yo.getX()) * dir;
        double alto = yo.getY() - b.getY();
        if (delante > 0 && delante < 95 && alto < 95) a.add(Accion.PATEAR);
        if (Math.abs(delante) < 110 && alto > 95 && alto < 260 && yo.estaEnSuelo()) a.add(Accion.SALTAR);
    }

    /** Se queda cerca de su arco y solo sale a atacar si el balón está en su mitad o viene hacia él. */
    protected void defender(Jugador yo, Balon b, Partida p, EnumSet<Accion> a) {
        double centro = p.getCancha().getCentroX();
        boolean enMiMitad = (b.getX() - centro) * yo.getDireccion() < 80;
        boolean amenaza = b.getVx() * yo.getDireccion() < -150;
        if (enMiMitad || amenaza) atacarBalon(yo, b, a);
        else irHacia(yo, Cancha.posicionInicialX(yo.getLado()) - yo.getDireccion() * 90, a);
    }

    /** Salta para esquivar un tiro de poder rival que viene hacia él. */
    protected void esquivarTiro(Jugador yo, Balon b, EnumSet<Accion> a) {
        if (!b.tienePoder() || b.getLanzador() == yo || !yo.estaEnSuelo()) return;
        boolean viene = (yo.getX() - b.getX()) * b.getVx() > 0;
        if (viene && Math.abs(yo.getX() - b.getX()) < 260 && b.getY() > yo.getY() - 110) a.add(Accion.SALTAR);
    }

    /** Hay un rival frente a mí, al alcance del pie y que no está aturdido. */
    protected boolean rivalAlAlcance(Jugador yo, Jugador rival) {
        double delante = (rival.getX() - yo.getX()) * yo.getDireccion();
        return delante > 0 && delante < 85 && Math.abs(rival.getY() - yo.getY()) < 80 && !rival.estaAturdido();
    }

    /** Activa el poder cuando la barra está llena, no tiene aura y el balón está cerca. */
    protected void usarPoderSiConviene(Jugador yo, Balon b, EnumSet<Accion> a) {
        if (yo.getMedidor().estaLleno() && !yo.tieneAura() && Math.abs(b.getX() - yo.getX()) < 260) a.add(Accion.PODER);
    }
}

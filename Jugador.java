package dominio;

import java.util.Set;

/**
 * Clase que representa un jugador que es una enrtidad
 */
public class Jugador extends Entidad {
    private final Lado lado;
    private final Personaje personaje;
    private final String nombre;
    private final Estadisticas estadisticas;
    private final Controlador controlador;
    private final MedidorPoder medidor = new MedidorPoder();
    private final Pie pie = new Pie();
    private final double xInicial;
    private final int direccion;

    private double vida;
    private double tiempoAturdido;
    private boolean aura;
    private Efecto efecto;
    private Efecto efectoTerminado;
    private double direccionMov;
    private double impulsoX;
    private boolean enSuelo = true;

    public Jugador(Lado lado, Personaje personaje, String nombre, Estadisticas estadisticas, Controlador controlador) {
        this.lado = lado;
        this.personaje = personaje;
        this.nombre = nombre;
        this.estadisticas = estadisticas;
        this.controlador = controlador;
        this.direccion = lado == Lado.IZQUIERDO ? 1 : -1;
        this.xInicial = Cancha.posicionInicialX(lado);
        this.vida = estadisticas.getVidaMaxima();
        reiniciarPosicion();
    }

    public void aplicarAcciones(Set<Accion> acciones) {
        direccionMov = 0;
        if (estaBloqueado()) return;
        boolean izq = acciones.contains(Accion.IZQUIERDA);
        boolean der = acciones.contains(Accion.DERECHA);
        if (izq && !der) direccionMov = -1;
        else if (der && !izq) direccionMov = 1;
        if (acciones.contains(Accion.SALTAR) && enSuelo && !bloqueaSalto()) {
            vy = -estadisticas.getImpulsoSalto();
            enSuelo = false;
        }
        if (acciones.contains(Accion.PATEAR)) pie.iniciar();
        if (acciones.contains(Accion.PODER)) activarPoder();
    }

    public void activarPoder() {
        if (!aura && medidor.estaLleno()) {
            medidor.vaciar();
            aura = true;
        }
    }

    public void dispararPoder(Balon balon) {
        personaje.getPoder().lanzar(balon, this);
        aura = false;
    }

    public void recibirDanio(double danio) {
        if (estaAturdido() || danio <= 0) return;
        vida = Math.max(0, vida - danio);
        if (vida <= 0) {
            tiempoAturdido = Reglas.DURACION_ATURDIMIENTO;
            pie.cancelar();
        }
    }

    public void agregarEfecto(Efecto nuevo, Partida partida) {
        if (efecto != null) efecto.alTerminar(partida, this);
        efecto = nuevo;
        nuevo.alAplicar(partida, this);
    }

    public Efecto limpiarEfecto() {
        Efecto e = efecto;
        efecto = null;
        efectoTerminado = null;
        return e;
    }

    public Efecto retirarEfectoTerminado() {
        Efecto e = efectoTerminado;
        efectoTerminado = null;
        return e;
    }

    public void fijarImpulso(double impulso) { 
        impulsoX = impulso; 
    }
    public void lanzarAlAire(double impulso) { 
        vy = -impulso; enSuelo = false; 
    }

    @Override
    public void actualizar(double dt, Cancha cancha) {
        if (tiempoAturdido > 0) {
            tiempoAturdido -= dt;
            if (tiempoAturdido <= 0) {            // al terminar el aturdimientorecupera su vida máxima
                tiempoAturdido = 0;
                vida = estadisticas.getVidaMaxima();
            }
        }
        if (efecto != null) {
            efecto.actualizar(dt);
            efecto.enCadaPaso(this, dt);
            if (efecto.haTerminado()) { efectoTerminado = efecto; efecto = null; }
        }

        double vel = estaBloqueado() ? 0 : getVelocidadActual();
        vx = direccionMov * vel + impulsoX;
        vy += Reglas.GRAVEDAD_JUGADOR * dt;
        x += vx * dt;
        y += vy * dt;
        impulsoX *= Math.exp(-8 * dt);

        if (y >= cancha.getYSuelo()) { y = cancha.getYSuelo(); vy = 0; enSuelo = true; }
        else enSuelo = false;

        if (x < Reglas.RADIO_TORSO) x = Reglas.RADIO_TORSO;
        if (x > cancha.getAncho() - Reglas.RADIO_TORSO) x = cancha.getAncho() - Reglas.RADIO_TORSO;

        pie.actualizar(dt);
        if (estaBloqueado()) pie.cancelar();
    }

    public void reiniciarPosicion() {
        x = xInicial; y = Reglas.Y_SUELO; vx = 0; vy = 0;
        impulsoX = 0; direccionMov = 0; enSuelo = true;
        pie.cancelar();
    }

    public boolean estaAturdido() { 
        return tiempoAturdido > 0; 
    }
    public boolean estaBloqueado() { 
        return estaAturdido() || (efecto != null && efecto.bloqueaAcciones()); 
    }
    private boolean bloqueaSalto() { 
        return efecto != null && efecto.bloqueaSalto(); 
    }

    public double getVelocidadActual() {
        return estadisticas.getVelocidad() * (efecto != null ? efecto.factorVelocidad() : 1.0);
    }

    public boolean esHumano() { return controlador instanceof ControladorHumano; }
    public Lado getLado() { return lado; }
    public int getDireccion() { return direccion; }
    public Personaje getPersonaje() { return personaje; }
    public String getNombre() { return nombre; }
    public Estadisticas getEstadisticas() { return estadisticas; }
    public Controlador getControlador() { return controlador; }
    public MedidorPoder getMedidor() { return medidor; }
    public Pie getPie() { return pie; }
    public boolean estaEnSuelo() { return enSuelo; }
    public boolean tieneAura() { return aura; }
    public Efecto getEfecto() { return efecto; }
    public double getVida() { return vida; }
    public double getVidaMaxima() { return estadisticas.getVidaMaxima(); }
    public double getTiempoAturdido() { return tiempoAturdido; }

    public Circulo getCirculoCabeza() { return new Circulo(x, y - Reglas.ALTURA_CABEZA, Reglas.RADIO_CABEZA); }
    public Circulo getCirculoTorso() { return new Circulo(x, y - Reglas.ALTURA_TORSO, Reglas.RADIO_TORSO); }
    public double getPivotePieY() { return y - Reglas.ALTURA_CADERA; }

    public double getPieX() {
        if (pie.estaActivo()) return x + direccion * Reglas.LARGO_PIERNA * Math.cos(pie.getAngulo());
        return x + direccion * 14;
    }

    public double getPieY() {
        if (pie.estaActivo()) {
            double py = getPivotePieY() - Reglas.LARGO_PIERNA * Math.sin(pie.getAngulo());
            return Math.min(py, y - 8);
        }
        return y - 9;
    }

    public Circulo getCirculoPie() { return new Circulo(getPieX(), getPieY(), Reglas.RADIO_PIE); }
}

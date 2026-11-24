package dominio;

import java.util.Set;

/**
 * Clase Jugador que extiende de entidad
 * Responsable del Jugador y todo lo que aplica y que le afecta
 * 
 * es Serializable para poder guardarlo y abrirlo
 * 
 * @author Bryan Pinzon y Valentina Prieto
 * @version Entrega Final Proyecto Final
 */

public class Jugador extends Entidad {
    // indentificador de serializacion
    private static final long serialVersionUID = 1L;
    // atributos
    private final Lado lado;
    private final Personaje personaje;
    private final String nombre;
    private final Estadisticas estadisticas;
    private final int puntos;
    // quien lo controla
    private final Controlador controlador;
    private final MedidorPoder medidor = new MedidorPoder();
    private final Pie pie = new Pie();
    // posicion x inicial
    private final double xInicial;
    private final int direccion;
    // mas atributos
    private double vida;
    private double tiempoAturdido;
    private boolean aura;
    private Efecto efecto;
    private Efecto efectoTerminado;
    private double direccionMov;
    private double impulsoX;
    private boolean enSuelo = true;

    /**
     * Constructor del jugador
     * @param lado lado de la cancha del qe va a jugar
     * @param personaje el personaje que selecciono
     * @param nombre el nombre del jugador
     * @param estadisticas Las estadisticas que tiene
     * @param puntos los puntos ue tiene
     * @param controlador quien lo controla
     */
    public Jugador(Lado lado, Personaje personaje, String nombre, Estadisticas estadisticas, int puntos, Controlador controlador) {
        this.lado = lado;
        this.personaje = personaje;
        this.nombre = nombre;
        this.estadisticas = estadisticas;
        this.puntos = puntos;
        this.controlador = controlador;
        this.direccion = lado == Lado.IZQUIERDO ? 1 : -1;
        this.xInicial = Cancha.posicionInicialX(lado);
        this.vida = estadisticas.getVidaMaxima();
        reiniciarPosicion();
    }

    /**
     * Metodo que traduce las acciones del controlidor
     * @param acciones
     */
    public void aplicarAcciones(Set<Accion> acciones) {
        direccionMov = 0;
        if (estaBloqueado()) return;
        boolean izq = acciones.contains(Accion.IZQUIERDA);
        boolean der = acciones.contains(Accion.DERECHA);
        // para moverse a la izquierda es negativo
        if (izq && !der) direccionMov = -1;
        // para moverse a la derecha es positivo
        else if (der && !izq) direccionMov = 1;
        if (acciones.contains(Accion.SALTAR) && enSuelo && !bloqueaSalto()) {
            // como si puede saltar calculamos su velocidad en y negativa (porque va hacia arriba)
            vy = -estadisticas.getImpulsoSalto();
            enSuelo = false;
        }
        // si patea
        if (acciones.contains(Accion.PATEAR)) pie.iniciar();
        // si activa un poder
        if (acciones.contains(Accion.PODER)) activarPoder();
    }

    /**
     * Metodo que activa el poder del personaje
     * lo activa si no tiene un poder activo y si su medidor esta lleno
     * tenemos que vaciar el medidor una vez se activa el poder
     */
    public void activarPoder() {
        if (!aura && medidor.estaLleno()) {
            medidor.vaciar();
            aura = true;
        }
    }

    /**
     * Metodo que dispara el balon segun su poder
     * @param balon
     */
    public void dispararPoder(Balon balon) {
        personaje.getPoder().lanzar(balon, this);
        aura = false;
    }

    /**
     * MEtodo que le resta vida al jugador al recibir danio 
     * si llega a cero su vida se queda aturdido 3 segundos para despues restaurar su vida
     * @param danio
     */
    public void recibirDanio(double danio) {
        if (estaAturdido() || danio <= 0) return;
        vida = Math.max(0, vida - danio);
        if (vida <= 0) {
            tiempoAturdido = Reglas.DURACION_ATURDIMIENTO;
            pie.cancelar();
        }
    }

    /**
     * Metodo que se encarga de los efectos cuando ocurre un efecto nuevo
     * @param nuevo
     * @param partida
     */
    public void agregarEfecto(Efecto nuevo, Partida partida) {
        if (efecto != null) efecto.alTerminar(partida, this);
        efecto = nuevo;
        nuevo.alAplicar(partida, this);
    }

    /**
     * Metodo que se encarga de quitar el efecto que este actualmente 
     * @return retorna el efecto para que la partida se encargue de cerrarlo
     */
    public Efecto limpiarEfecto() {
        Efecto e = efecto;
        efecto = null;
        efectoTerminado = null;
        return e;
    }

    /**
     * Metodo que quita un efecto por tiempo
     * @return
     */
    public Efecto retirarEfectoTerminado() {
        Efecto e = efectoTerminado;
        efectoTerminado = null;
        return e;
    }

    /**
     * Metodo que fija el impilso
     * @param impulso
     */
    public void fijarImpulso(double impulso) { 
        impulsoX = impulso; 
    }

    /**
     * Metodo que pasa al aire salta segun el impulso
     * @param impulso
     */
    public void lanzarAlAire(double impulso) { 
        vy = -impulso; 
        enSuelo = false; 
    }

    /** 
     * Metodo que actualiza al jugador respecto al tiempo
     * 
     * @see dominio.Entidad#actualizar(double, dominio.Cancha)
     */
    @Override
    public void actualizar(double dt, Cancha cancha) {
        if (tiempoAturdido > 0) {
            tiempoAturdido -= dt;
            if (tiempoAturdido <= 0) { // al terminar el aturdimiento recupera su vida maxima
                tiempoAturdido = 0;
                vida = estadisticas.getVidaMaxima();
            }
        }
        // si tiene un efecto actibo
        if (efecto != null) {
            efecto.actualizar(dt);
            efecto.enCadaPaso(this, dt);
            // si se termina el efecto lo dejamos null
            if (efecto.haTerminado()) { 
                efectoTerminado = efecto; 
                efecto = null; 
            }
        }
        // si esta bloqueado no tiene velocidad
        double vel = estaBloqueado() ? 0 : getVelocidadActual();
        // calculamos la velocidad en x segun hacia donde se mueve, la velocidad y su impulso
        vx = direccionMov * vel + impulsoX;
        // calculamos la velocidad en y segun la gravedad
        vy += Reglas.GRAVEDAD_JUGADOR * dt;
        // calculamos la posicion en x y y con la velocidad y el tiempo respectivamente
        x += vx * dt;
        y += vy * dt;
        impulsoX *= Math.exp(-8 * dt);

        // Si toca o pasa el suelo 
        if (y >= cancha.getYSuelo()) { 
            //lo ubicamos justo por encima del suelo
            y = cancha.getYSuelo(); 
            vy = 0; 
            enSuelo = true; 
        }
        else enSuelo = false;
        // Si toca o pasa alguna de las paredes lo ubicamos justo a raz con ellas
        if (x < Reglas.RADIO_TORSO) x = Reglas.RADIO_TORSO;
        if (x > cancha.getAncho() - Reglas.RADIO_TORSO) x = cancha.getAncho() - Reglas.RADIO_TORSO;
        // vamos actualizando el pie
        pie.actualizar(dt);
        if (estaBloqueado()) pie.cancelar();
    }

    /**
     * Metodo que vuelve a ubicar al jugador en su cancha
     * con sus valores y estados iniciales
     */
    public void reiniciarPosicion() {
        x = xInicial; 
        y = Reglas.Y_SUELO; 
        vx = 0; 
        vy = 0;
        impulsoX = 0; 
        direccionMov = 0; 
        enSuelo = true;
        pie.cancelar();
    }

    // Metodos getter
    public boolean estaAturdido(){ 
        return tiempoAturdido > 0; 
    }
    public boolean estaBloqueado(){ 
        return estaAturdido() || (efecto != null && efecto.bloqueaAcciones()); 
    }
    private boolean bloqueaSalto(){ 
        return efecto != null && efecto.bloqueaSalto(); 
    }
    public double getVelocidadActual(){
        return estadisticas.getVelocidad() * (efecto != null ? efecto.factorVelocidad() : 1.0);
    }
    public boolean esHumano(){ 
        return controlador instanceof ControladorHumano; 
    }
    public Lado getLado(){ 
        return lado; 
    }
    public int getDireccion(){ 
        return direccion; 
    }
    public Personaje getPersonaje(){ 
        return personaje; 
    }
    public String getNombre(){ 
        return nombre; 
    }
    public Estadisticas getEstadisticas(){ 
        return estadisticas; 
    }
    public int getPuntos(){ 
        return puntos; 
    }
    public Controlador getControlador(){ 
        return controlador; 
    }
    public MedidorPoder getMedidor(){ 
        return medidor; 
    }
    public Pie getPie(){ 
        return pie; 
    }
    public boolean estaEnSuelo(){ 
        return enSuelo; 
    }
    public boolean tieneAura(){ 
        return aura; 
    }
    public Efecto getEfecto(){ 
        return efecto; 
    }
    public double getVida(){ 
        return vida; 
    }
    public double getVidaMaxima(){ 
        return estadisticas.getVidaMaxima(); 
    }
    public double getTiempoAturdido(){ 
        return tiempoAturdido; 
    }

    /**
     * Metodo que nos da el circulo de la cabeza
     * @return
     */
    public Circulo getCirculoCabeza(){ 
        return new Circulo(x, y - Reglas.ALTURA_CABEZA, Reglas.RADIO_CABEZA); 
    }
    /**
     * Metodo que nos da el circulo del torso
     * @return
     */
    public Circulo getCirculoTorso(){ 
        return new Circulo(x, y - Reglas.ALTURA_TORSO, Reglas.RADIO_TORSO); 
    }
    /**
     * Metodo que nos da el pivot del pie en y
     * @return
     */
    public double getPivotePieY() { 
        return y - Reglas.ALTURA_CADERA; 
    }

    // Metodos del pie
    public double getPieX(){
        if (pie.estaActivo()) return x + direccion * Reglas.LARGO_PIERNA * Math.cos(pie.getAngulo());
        return x + direccion * 14;
    }
    public double getPieY(){
        if (pie.estaActivo()){
            double py = getPivotePieY() - Reglas.LARGO_PIERNA * Math.sin(pie.getAngulo());
            return Math.min(py, y - 8);
        }
        return y - 9;
    }
    public Circulo getCirculoPie(){ 
        return new Circulo(getPieX(), getPieY(), Reglas.RADIO_PIE); 
    }
}

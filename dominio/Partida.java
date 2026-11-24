package dominio;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Clase Partida
 * Responsable de coordinar los elementos de la partida y sis reglas
 * va avanzando con actualizar dt y su forma de avisar es con partidaListener
 * 
 * es Serializable para poder guardarlo y abrirlo
 * 
 * @author Bryan Pinzon y Valentina Prieto y ayuda IA
 * @version Entrega Final Proyecto Final
 */

public class Partida implements Serializable {
    // identificador de serializacion
    private static final long serialVersionUID = 1L;
    // Todos los elementos que intervienen en la cancha
    private final Cancha cancha = new Cancha();
    private final Balon balon = new Balon();
    private final List<Balon> balonesFalsos = new ArrayList<Balon>();
    private final Jugador[] jugadores = new Jugador[2];
    private final Marcador marcador = new Marcador();
    private final TipoPartida tipo;
    private transient NotificadorPartida notificador = new NotificadorPartida();
    private transient MotorFisica motor = new MotorFisica(notificador);
    // estados y tiempos iniciales
    private EstadoPartida estado = EstadoPartida.CUENTA_REGRESIVA;
    private EstadoPartida estadoAntesDePausa = EstadoPartida.JUGANDO;
    private double temporizador = Reglas.DURACION_CUENTA_REGRESIVA;
    private double tiempoJugado;
    private Lado ultimoAnotador;
    private boolean ultimoAutogol;
    private boolean terminadaPorUsuario;
    private ResultadoPartida resultado;

    /**
     * Constructor de la partida
     * @param izquierdo
     * @param derecho
     * @param tipo
     */
    public Partida(Jugador izquierdo, Jugador derecho, TipoPartida tipo) {
        jugadores[Lado.IZQUIERDO.ordinal()] = izquierdo;
        jugadores[Lado.DERECHO.ordinal()] = derecho;
        this.tipo = tipo;
        // segun el tipo de la partida la iniciamos
        tipo.iniciar(this);
        reiniciarPosiciones();
    }

    /**
     * Metodo que permite recuperar los elementos serializados
     * (Metodo propuesto y mejorado con ayuda de IA)
     * @param in
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        notificador = new NotificadorPartida();
        motor = new MotorFisica(notificador);
    }

    /**
     * Metodo que se encarga del ciclo principal del juego que avanza en segundos y respecto a su estado
     * @param dt
     */
    public void actualizar(double dt) {
        // segun el estado pasa algo
        switch (estado) {
            case CUENTA_REGRESIVA:
                // una vez el temporizador llegue a 0 empezamos la partida (cambia a estado JUGANDO)
                temporizador -= dt;
                if (temporizador <= 0) cambiarEstado(EstadoPartida.JUGANDO);
                break;
            case JUGANDO:
                // si ya esta jugando vamos avanzando los elementos
                avanzar(dt);
                break;
            case GOL:
                // Cuando se hace gol volvemos al temporizador
                temporizador -= dt;
                // si el temporizador ya llego a 0 tenemos 2 opciones
                if (temporizador <= 0) {
                    // si y ya termino el partido lo terminamos
                    if (tipo.haTerminado()) {
                        finalizar();
                    } else {
                        // si no volvemos a jugar en posiciones iniciales
                        reiniciarPosiciones();
                        cambiarEstado(EstadoPartida.JUGANDO);
                    }
                }
                break;
            case FIN_PARTIDO:
                // si ya se termino volvemos al temporizador
                temporizador -= dt;
                // una vez llegue a 0 lo finalizamos
                if (temporizador <= 0) finalizar();
                break;
            default:
                break;
        }
    }

    /**
     * Metodo que avanza el partido segun su tipo
     * (Metodo mejorado con ayuda de IA)
     * @param dt
     */
    private void avanzar(double dt) {
        // llevamos el tiempo que ha jugado
        tiempoJugado += dt;
        // hacemos que vaya pasando el tiempo segun el tipo de la partida
        tipo.alPasarElTiempo(this, dt);
        // vemos si ua se termino o no para determinar su estado
        if (tipo.haTerminado()) {
            temporizador = Reglas.DURACION_FIN;
            cambiarEstado(EstadoPartida.FIN_PARTIDO);
            return;
        }
        // guardamos el estado de aturdimiento de los jugadores
        boolean[] aturdidoAntes = new boolean[2];
        for (int i = 0; i < 2; i++) {
            Jugador j = jugadores[i];
            aturdidoAntes[i] = j.estaAturdido();
            //revisamos las acciones 
            boolean pateaba = j.getPie().estaActivo();
            boolean teniaAura = j.tieneAura();
            // aplica las acciones con el controlador
            j.aplicarAcciones(j.getControlador().decidir(j, this, dt));
            // notifica las acciones 
            if (!pateaba && j.getPie().estaActivo()) notificador.alPatada(j);
            if (!teniaAura && j.tieneAura()) notificador.alActivarPoder(j);
            j.getMedidor().recargar(dt);
            // actualiza el jugador
            j.actualizar(dt, cancha);
            // revisa los ofectos
            Efecto fin = j.retirarEfectoTerminado();
            if (fin != null) fin.alTerminar(this, j);
        }
        // actualiza el/los balones
        balon.actualizar(dt, cancha);
        for (Balon f : balonesFalsos) f.actualizar(dt, cancha);
        // el motor resuelve las fisicas
        motor.resolver(this);
        // colcemos a recorrer los jugadores y los comparamos respecto al estado registrado antes de actualizar
        for (int i = 0; i < 2; i++) {
            if (!aturdidoAntes[i] && jugadores[i].estaAturdido()) notificador.alAturdirse(jugadores[i]);
        }
        // vemos si se metio algun gol y si algo lo registramos
        Lado arco = motor.detectarGol(this);
        if (arco != null) registrarGol(arco);
    }

    /**
     * Metodo que registra el gol considerando donde entro (punto para el contrario)
     * @param arcoDondeEntro
     */
    private void registrarGol(Lado arcoDondeEntro) {
        // obtenemos el lado que anoto (opuesto a donde entro)
        Lado anotador = arcoDondeEntro.opuesto();
        // consideramos el ultimo toque al balon (para determinar gol o autogol)
        Jugador ultimo = balon.getUltimoToque();
        // definimos si es un autogol
        ultimoAutogol = ultimo != null && ultimo.getLado() == arcoDondeEntro;
        // determinamos quien anota el punto
        ultimoAnotador = anotador;
        // sumamos el gol a quien anoto
        marcador.sumarGol(anotador);
        // a quien le metieron gol le sumamos su barra de poder
        getJugador(arcoDondeEntro).getMedidor().sumarGolRecibido();
        // avisamos el gol
        tipo.alAnotarse(this, anotador);
        //dejamos el espaco de celebracion
        temporizador = Reglas.DURACION_CELEBRACION;
        cambiarEstado(EstadoPartida.GOL);
        // notificamos
        notificador.alGol(anotador, ultimoAutogol);
    }

    /**
     * Metodo que se encarga de poner los jugadores y el balon en su estado/posicion inicial
     */
    private void reiniciarPosiciones() {
        for (Jugador j : jugadores) {
            Efecto e = j.limpiarEfecto();
            if (e != null) e.alTerminar(this, j);
            j.reiniciarPosicion();
        }
        eliminarBalonesFalsos();
        balon.reiniciar(cancha.getCentroX(), 150);
    }

    /**
     * Metodo que terminao cierra el partido con su resultado y tambien notifica
     */
    private void finalizar() {
        resultado = construirResultado();
        cambiarEstado(EstadoPartida.FINALIZADA);
        notificador.alFinalizar(resultado);
    }

    /**
     * Metodo que construye el resultado del partido  
     * @return
     */
    private ResultadoPartida construirResultado() {
        return new ResultadoPartida(terminadaPorUsuario ? null : tipo.getGanador(), getJugador(Lado.IZQUIERDO), getJugador(Lado.DERECHO),
                marcador.getGoles(Lado.IZQUIERDO), marcador.getGoles(Lado.DERECHO), tipo.enMuerteSubita(), terminadaPorUsuario, tiempoJugado);
    }

    /**
     * Metodo qye termina el juego en cualquier momento (por un jugador)
     * no tiene ni ganador ni puntos si termina de etsa manera
     */
    public void terminarManualmente() {
        if (estado == EstadoPartida.FINALIZADA) return;
        terminadaPorUsuario = true;
        finalizar();
    }

    /**
     * Metodo que crea los balonees falsos
     * @param cantidad de balones a crear
     */
    public void crearBalonesFalsos(int cantidad) {
        // limpiamos balones falsos
        eliminarBalonesFalsos();
        Random r = new Random();
        for (int i = 0; i < cantidad; i++) {
            // generamos un angulo aleatoreo
            double ang = Math.toRadians(-160 + r.nextInt(140));
            // generamos una velocidad aleatorea
            double v = 250 + r.nextInt(600);
            // creamos los balones falsos y los agregamos a nuestra lista
            balonesFalsos.add(balon.crearFalso(Math.cos(ang) * v, Math.sin(ang) * v));
        }
    }
    /**
     * MEtodo que se encarga de limpiar la lista de balones falsos
     */
    public void eliminarBalonesFalsos() { 
        balonesFalsos.clear(); 
    }

    /**
     * Metodo que pone el estado de la partida en pausada
     */
    public void pausar() {
        if (estado == EstadoPartida.PAUSADA || estado == EstadoPartida.FINALIZADA) return;
        estadoAntesDePausa = estado;
        cambiarEstado(EstadoPartida.PAUSADA);
    }

    /**
     * Metodo que devuelve al estado antes de pausar la partida
     */
    public void reanudar() {
        if (estado == EstadoPartida.PAUSADA) cambiarEstado(estadoAntesDePausa);
    }

    /**
     * Metodo que nos permite cambiar el estado de la partidaa otro
     * @param nuevo
     */
    private void cambiarEstado(EstadoPartida nuevo) {
        estado = nuevo;
        notificador.alCambiarEstado(nuevo);
    }

    // Metodos para agregar y quitar listeners
    // metodos propuestos con ayuda de IA
    public void agregarListener(PartidaListener l) { notificador.agregar(l); }
    public void quitarListener(PartidaListener l) { notificador.quitar(l); }

    // metodos getter del partido
    public Cancha getCancha(){ 
        return cancha; 
    }
    public Balon getBalon(){ 
        return balon; 
    }
    public List<Balon> getBalonesFalsos(){ 
        return balonesFalsos; 
    }
    public Jugador getJugador(Lado lado){ 
        return jugadores[lado.ordinal()]; 
    }
    public Marcador getMarcador(){ 
        return marcador; 
    }
    public TipoPartida getTipo(){ 
        return tipo; 
    }
    public EstadoPartida getEstado(){ 
        return estado; 
    }
    public double getTemporizador(){ 
        return temporizador; 
    }
    public double getTiempoJugado(){ 
        return tiempoJugado; 
    }
    public Lado getUltimoAnotador(){ 
        return ultimoAnotador; 
    }
    public boolean isUltimoAutogol(){ 
        return ultimoAutogol; 
    }
    public ResultadoPartida getResultado(){ 
        return resultado != null ? resultado : construirResultado(); 
    }
}

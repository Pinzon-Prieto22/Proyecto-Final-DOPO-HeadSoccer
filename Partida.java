package dominio;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Clase responsable de la partida
 * 
 * se encarga de la cancha el balos, los jugadores, el marcador y las reglas
 * con PartidaListener va avisando
 */
public class Partida {
    private final Cancha cancha = new Cancha();
    private final Balon balon = new Balon();
    private final List<Balon> balonesFalsos = new ArrayList<Balon>();
    private final Jugador[] jugadores = new Jugador[2];
    private final Marcador marcador = new Marcador();
    private final TipoPartida tipo;
    private final NotificadorPartida notificador = new NotificadorPartida();
    private final MotorFisica motor = new MotorFisica(notificador);

    private EstadoPartida estado = EstadoPartida.CUENTA_REGRESIVA;
    private EstadoPartida estadoAntesDePausa = EstadoPartida.JUGANDO;
    private double temporizador = Reglas.DURACION_CUENTA_REGRESIVA;
    private double tiempoJugado;
    private Lado ultimoAnotador;
    private boolean ultimoAutogol;
    private boolean terminadaPorUsuario;
    private ResultadoPartida resultado;

    public Partida(Jugador izquierdo, Jugador derecho, TipoPartida tipo) {
        jugadores[Lado.IZQUIERDO.ordinal()] = izquierdo;
        jugadores[Lado.DERECHO.ordinal()] = derecho;
        this.tipo = tipo;
        tipo.iniciar(this);
        reiniciarPosiciones();
    }

    public void actualizar(double dt) {
        switch (estado) {
            case CUENTA_REGRESIVA:
                temporizador -= dt;
                if (temporizador <= 0) cambiarEstado(EstadoPartida.JUGANDO);
                break;
            case JUGANDO:
                avanzar(dt);
                break;
            case GOL:
                temporizador -= dt;
                if (temporizador <= 0) {
                    if (tipo.haTerminado()) {
                        finalizar();
                    } else {
                        reiniciarPosiciones();
                        cambiarEstado(EstadoPartida.JUGANDO);
                    }
                }
                break;
            case FIN_PARTIDO:
                temporizador -= dt;
                if (temporizador <= 0) finalizar();
                break;
            default:
                break;
        }
    }

    private void avanzar(double dt) {
        tiempoJugado += dt;
        tipo.alPasarElTiempo(this, dt);
        if (tipo.haTerminado()) {
            temporizador = Reglas.DURACION_FIN;
            cambiarEstado(EstadoPartida.FIN_PARTIDO);
            return;
        }
        boolean[] aturdidoAntes = new boolean[2];
        for (int i = 0; i < 2; i++) {
            Jugador j = jugadores[i];
            aturdidoAntes[i] = j.estaAturdido();
            boolean pateaba = j.getPie().estaActivo();
            boolean teniaAura = j.tieneAura();
            j.aplicarAcciones(j.getControlador().decidir(j, this, dt));
            if (!pateaba && j.getPie().estaActivo()) notificador.alPatada(j);
            if (!teniaAura && j.tieneAura()) notificador.alActivarPoder(j);
            j.getMedidor().recargar(dt);
            j.actualizar(dt, cancha);
            Efecto fin = j.retirarEfectoTerminado();
            if (fin != null) fin.alTerminar(this, j);
        }
        balon.actualizar(dt, cancha);
        for (Balon f : balonesFalsos) f.actualizar(dt, cancha);
        motor.resolver(this);
        for (int i = 0; i < 2; i++) {
            if (!aturdidoAntes[i] && jugadores[i].estaAturdido()) notificador.alAturdirse(jugadores[i]);
        }
        Lado arco = motor.detectarGol(this);
        if (arco != null) registrarGol(arco);
    }

    private void registrarGol(Lado arcoDondeEntro) {
        Lado anotador = arcoDondeEntro.opuesto();
        Jugador ultimo = balon.getUltimoToque();
        ultimoAutogol = ultimo != null && ultimo.getLado() == arcoDondeEntro;
        ultimoAnotador = anotador;
        marcador.sumarGol(anotador);
        getJugador(arcoDondeEntro).getMedidor().sumarGolRecibido();   // su barra sube un cuarto de inmediato
        tipo.alAnotarse(this, anotador);
        temporizador = Reglas.DURACION_CELEBRACION;
        cambiarEstado(EstadoPartida.GOL);
        notificador.alGol(anotador, ultimoAutogol);
    }

    private void reiniciarPosiciones(){
        for (Jugador j : jugadores) {
            Efecto e = j.limpiarEfecto();
            if (e != null) e.alTerminar(this, j);
            j.reiniciarPosicion();
        }
        eliminarBalonesFalsos();
        balon.reiniciar(cancha.getCentroX(), 150);
    }

    private void finalizar(){
        resultado = construirResultado();
        cambiarEstado(EstadoPartida.FINALIZADA);
        notificador.alFinalizar(resultado);
    }

    private ResultadoPartida construirResultado(){
        return new ResultadoPartida(terminadaPorUsuario ? null : tipo.getGanador(),
                getJugador(Lado.IZQUIERDO), getJugador(Lado.DERECHO),
                marcador.getGoles(Lado.IZQUIERDO), marcador.getGoles(Lado.DERECHO),
                tipo.enMuerteSubita(), terminadaPorUsuario, tiempoJugado);
    }

    public void terminarManualmente() {
        if (estado == EstadoPartida.FINALIZADA) return;
        terminadaPorUsuario = true;
        finalizar();
    }

    public void crearBalonesFalsos(int cantidad){
        eliminarBalonesFalsos();
        Random r = new Random();
        for (int i = 0; i < cantidad; i++) {
            double ang = Math.toRadians(-160 + r.nextInt(140));
            double v = 250 + r.nextInt(600);
            balonesFalsos.add(balon.crearFalso(Math.cos(ang) * v, Math.sin(ang) * v));
        }
    }

    public void eliminarBalonesFalsos(){ 
        balonesFalsos.clear(); 
    }

    public void pausar(){
        if (estado == EstadoPartida.PAUSADA || estado == EstadoPartida.FINALIZADA) return;
        estadoAntesDePausa = estado;
        cambiarEstado(EstadoPartida.PAUSADA);
    }

    public void reanudar() {
        if (estado == EstadoPartida.PAUSADA) cambiarEstado(estadoAntesDePausa);
    }

    private void cambiarEstado(EstadoPartida nuevo){
        estado = nuevo;
        notificador.alCambiarEstado(nuevo);
    }

    public void agregarListener(PartidaListener l) { 
        notificador.agregar(l); 
    }
    public void quitarListener(PartidaListener l) { 
        notificador.quitar(l); 
    }

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
    public TipoPartida getTipo() { 
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
    public boolean isUltimoAutogol() { 
        return ultimoAutogol; 
    }
    public ResultadoPartida getResultado() { 
        return resultado != null ? resultado : construirResultado(); 
    }
}

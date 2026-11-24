package dominio;

/**
 * Clase balon que extiende de Entidad
 * Responsable del balon que lo afecta la gravedad, tiene rebotes
 * 
 * es Serializable para poder guardarlo y abrirlo
 * 
 * @author Bryan Pinzon y Valentina Prieto
 * @version Entrega Final Proyecto Final
 */

public class Balon extends Entidad {
    // identificador de serializacion
    private static final long serialVersionUID = 1L;
    // dimension del balon
    private final double radio = Reglas.RADIO_BALON;

    // condiciones y estados del balon
    private Jugador ultimoToque;
    private Poder poder;
    private Jugador lanzador;
    private double angulo;
    private boolean falso;

    // Metodos getter
    public double getRadio(){ 
        return radio; 
    }
    public double getAngulo(){ 
        return angulo; 
    }
    public boolean esFalso(){ 
        return falso; 
    }
    public Jugador getUltimoToque(){ 
        return ultimoToque; 
    }

    // Metodos setter
    public void setUltimoToque(Jugador j){ 
        this.ultimoToque = j; 
    }

    /**
     * Metodo que nos dice si lo afecta un poder
     * @return true si si tiene un poder
     */
    public boolean tienePoder(){ 
        return poder != null; 
    }
    /**
     * Metodo que nos da el poder qu elo esta afectando
     * @return el poder
     */
    public Poder getPoder() { 
        return poder; 
    }
    /**
     * Metodo que nos da el juugadroe lanzador
     * @return
     */
    public Jugador getLanzador() { 
        return lanzador; 
    }

    /**
     * Metodo que lanza el balon con poder (define sus estados)
     * @param poder poder
     * @param lanzador el jugarod del poder
     * @param vx evlocidad en x
     * @param vy velocidad en y
     */
    public void lanzarConPoder(Poder poder, Jugador lanzador, double vx, double vy) {
        this.poder = poder;
        this.lanzador = lanzador;
        this.ultimoToque = lanzador;
        this.vx = vx;
        this.vy = vy;
    }

    /**
     * Metodo que le quita el poder
     */
    public void descartarPoder(){ 
        poder = null; 
        lanzador = null; 
    }
    /**
     * Metodo que se encarga de frenar el balon
     * @param factor de frenado
     */
    public void frenar(double factor){ 
        vx *= factor; 
        vy *= factor; 
    }

    /**
     * Metodo que crea un balon falso
     * @param vx nueva velocidad
     * @param vy nueva velocidad
     * @return
     */
    public Balon crearFalso(double vx, double vy) {
        Balon f = new Balon();
        f.falso = true;
        f.x = x; 
        f.y = y; 
        f.vx = vx; 
        f.vy = vy;
        return f;
    }

    /**
     * MEtodo que reinicia el balon, lo deja quieto en la posicion x,y
     * para el inicio del partido y despues de cada gol para que vielva al centro o estado inicial
     * @param x posicion de inicio
     * @param y posicion de inicio
     */
    public void reiniciar(double x, double y) {
        this.x = x; 
        this.y = y; 
        vx = 0; 
        vy = 0; 
        angulo = 0;
        poder = null; 
        lanzador = null; 
        ultimoToque = null;
    }

    /** 
     * Metodo que actualiza el balon en el tiempo
     * 
     * @see dominio.Entidad#actualizar(double, dominio.Cancha)
     */
    @Override
    public void actualizar(double dt, Cancha cancha) {
        // calculamos como le afecta la gravedad
        double g = Reglas.GRAVEDAD_BALON * (poder != null ? poder.getFactorGravedad() : 1.0);
        // modificamos su velociaad en y considerando la gravedad
        vy += g * dt;
        // Lo movemos en ambis ejes
        x += vx * dt;
        y += vy * dt;
        if (poder == null && y + radio >= cancha.getYSuelo() - 0.5 && Math.abs(vy) < 5) {
            vx *= Math.max(0, 1 - Reglas.FRICCION_SUELO * dt);   // rueda y se frena
        }
        // consideramos el angulo
        angulo += vx / radio * dt;
        // consideramos los limites
        aplicarLimites(cancha);
        // consideramos la velocidad 
        limitarVelocidad();
    }

    /**
     * Metodo que considera los limites de la cancha 
     * como para los rebotes
     * @param c
     */
    public void aplicarLimites(Cancha c) {
        boolean reboteSuelo = false;
        boolean reboteOtro = false;
        // si el balon toca/pasa el suelo
        if (y + radio > c.getYSuelo()) {
            // calculamos donde quedaria el balon justo sobre el suelo
            y = c.getYSuelo() - radio;
            // si el balon tiene velocidad en y positiva (va hacia abajo)
            if (vy > 0) {
                // calculamos el rebote (negativo porque rebota hacia arriba)
                vy = -vy * (poder != null ? poder.getReboteSuelo() : Reglas.REBOTE_SUELO);
                if (poder == null && Math.abs(vy) < 60) vy = 0; // si el rebote es menor a 60 no rebota
                //ponemos que reboto con el suelo
                reboteSuelo = true;
            }
        }
        // si el balon toca/pasa el techo o limite superior
        if (y - radio < 0) {
            // calculamos donde quedaria el balon justo tocando el techo
            y = radio;
            // si el balon tiene velocidad y negativa (va subiendo) 
            if (vy < 0) { 
                // calculamos el rebote contra la pared
                vy = -vy * Reglas.REBOTE_PARED; 
                reboteOtro = true; 
            }
        }
        // si el balon toca/pasa la pared izqioerda
        if (x - radio < 0) {
            // calculamos donde quedaria el balon justo tocando la pared
            x = radio;
            // si va con velocidad horizontal negativa (va hacia la izquierda)
            if (vx < 0) { 
                // calculamos su rebote con la pared
                vx = -vx * Reglas.REBOTE_PARED; 
                reboteOtro = true; 
            }
        }
        // si el balon toca/pasa la pared derecha
        if (x + radio > c.getAncho()) {
            // calculamos donde quedaria el balon justo tocando la pared
            x = c.getAncho() - radio;
            // si va con velocidad horizontal positiva (v ahacia la derecha)
            if (vx > 0) { 
                // calculamos su rebote con la pared
                vx = -vx * Reglas.REBOTE_PARED; 
                reboteOtro = true; 
            }
        }
        // si tenia poder y llego a rebotar con algo se cancela el poder
        if (poder != null && (reboteOtro || (reboteSuelo && !poder.sobreviveRebotesEnSuelo()))) descartarPoder();
    }

    /**
     * Metodo que considea los limites de velocidad
     */
    public void limitarVelocidad() {
        //calculamos la velocidad totl del balon
        double v = Math.hypot(vx, vy);
        // si tiene una velocidad mucho mas rapida que la permitida
        if (v > Reglas.VELOCIDAD_MAXIMA_BALON) {
            // la reducimos proporcionalmente con el factor de reduccion
            double k = Reglas.VELOCIDAD_MAXIMA_BALON / v;
            vx *= k; 
            vy *= k;
        }
    }
}

package dominio;

public class Balon extends Entidad {
    private final double radio = Reglas.RADIO_BALON;
    private Jugador ultimoToque;
    private Poder poder;
    private Jugador lanzador;
    private double angulo;
    private boolean falso;

    public double getRadio() { 
        return radio; 
    }
    public double getAngulo() { 
        return angulo; 
    }
    public boolean esFalso() { 
        return falso; 
    }
    public Jugador getUltimoToque() { 
        return ultimoToque; 
    }
    public void setUltimoToque(Jugador j) { 
        this.ultimoToque = j; 
    }

    public boolean tienePoder() { 
        return poder != null; 
    }
    public Poder getPoder() { 
        return poder; 
    }
    public Jugador getLanzador() { 
        return lanzador; 
    }

    public void lanzarConPoder(Poder poder, Jugador lanzador, double vx, double vy) {
        this.poder = poder;
        this.lanzador = lanzador;
        this.ultimoToque = lanzador;
        this.vx = vx;
        this.vy = vy;
    }

    public void descartarPoder(){ 
        poder = null; lanzador = null; 
    }
    public void frenar(double factor){ 
        vx *= factor; vy *= factor; 
    }

    public Balon crearFalso(double vx, double vy) {
        Balon f = new Balon();
        f.falso = true;
        f.x = x; f.y = y; f.vx = vx; f.vy = vy;
        return f;
    }

    public void reiniciar(double x, double y) {
        this.x = x; this.y = y; vx = 0; vy = 0; angulo = 0;
        poder = null; lanzador = null; ultimoToque = null;
    }

    @Override
    public void actualizar(double dt, Cancha cancha) {
        double g = Reglas.GRAVEDAD_BALON * (poder != null ? poder.getFactorGravedad() : 1.0);
        vy += g * dt;
        x += vx * dt;
        y += vy * dt;
        if (poder == null && y + radio >= cancha.getYSuelo() - 0.5 && Math.abs(vy) < 5) {
            vx *= Math.max(0, 1 - Reglas.FRICCION_SUELO * dt);   // rueda y se frena
        }
        angulo += vx / radio * dt;
        aplicarLimites(cancha);
        limitarVelocidad();
    }

    public void aplicarLimites(Cancha c) {
        boolean reboteSuelo = false, reboteOtro = false;
        if (y + radio > c.getYSuelo()) {
            y = c.getYSuelo() - radio;
            if (vy > 0) {
                vy = -vy * (poder != null ? poder.getReboteSuelo() : Reglas.REBOTE_SUELO);
                if (poder == null && Math.abs(vy) < 60) vy = 0;
                reboteSuelo = true;
            }
        }
        if (y - radio < 0) {
            y = radio;
            if (vy < 0) { vy = -vy * Reglas.REBOTE_PARED; reboteOtro = true; }
        }
        if (x - radio < 0) {
            x = radio;
            if (vx < 0) { vx = -vx * Reglas.REBOTE_PARED; reboteOtro = true; }
        }
        if (x + radio > c.getAncho()) {
            x = c.getAncho() - radio;
            if (vx > 0) { vx = -vx * Reglas.REBOTE_PARED; reboteOtro = true; }
        }
        if (poder != null && (reboteOtro || (reboteSuelo && !poder.sobreviveRebotesEnSuelo()))) descartarPoder();
    }

    public void limitarVelocidad() {
        double v = Math.hypot(vx, vy);
        if (v > Reglas.VELOCIDAD_MAXIMA_BALON) {
            double k = Reglas.VELOCIDAD_MAXIMA_BALON / v;
            vx *= k; vy *= k;
        }
    }
}

package dominio;

/**
 * Clase abstracta que representa el poder, cada poder se define con sus especificaciones
 */
public abstract class Poder {
    private final String id;
    private final String nombre;
    private final String descripcion;
    private final double danioBase;

    protected Poder(String id, String nombre, String descripcion, double danioBase) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.danioBase = danioBase;
    }

    public String getId(){ 
        return id; 
    }
    public String getNombre(){ 
        return nombre; 
    }
    public String getDescripcion(){ 
        return descripcion; 
    }
    public double getDanioBase(){ 
        return danioBase; 
    }

    public abstract Efecto crearEfecto();
    
    protected abstract double getVelocidadTiro();

    protected double getInclinacion() { 
        return Reglas.INCLINACION_PODER; 
    }

    public double getFactorGravedad() { 
        return Reglas.FACTOR_GRAVEDAD_PODER; 
    }
    public double getReboteSuelo() { 
        return Reglas.REBOTE_SUELO; 
    }
    
    public boolean sobreviveRebotesEnSuelo() { return false; }
    
    protected void prepararBalon(Balon balon) { }

    public void lanzar(Balon balon, Jugador autor) {
        prepararBalon(balon);
        double dx = autor.getDireccion(), dy = getInclinacion();
        double n = Math.hypot(dx, dy), v = getVelocidadTiro();
        balon.lanzarConPoder(this, autor, dx / n * v, dy / n * v);
    }

    public double calcularDanio(Jugador autor) {
        return danioBase * autor.getEstadisticas().getMultiplicadorKick();
    }

    public Efecto aplicarA(Jugador victima, Jugador autor, Partida partida) {
        victima.recibirDanio(calcularDanio(autor));
        Efecto e = crearEfecto();
        victima.agregarEfecto(e, partida);
        return e;
    }

    @Override
    public String toString(){ 
        return nombre; 
    }
}

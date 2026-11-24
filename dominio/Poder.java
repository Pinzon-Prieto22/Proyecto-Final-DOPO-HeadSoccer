package dominio;

import java.io.Serializable;

/**
 * Clase abstracta Poder
 * Responsable de decir lo que debe definir cada poder respectivamente
 * Para agregar un poder extendemos de esta clase e implementamos
 * 
 * es Serializable para poder guardarlo y abrirlo
 * 
 * @author Bryan Pinzon y Valentina Prieto
 * @version Entrega Final Proyecto Final
 */

public abstract class Poder implements Serializable {
    //identificador de serializacion
    private static final long serialVersionUID = 1L;
    private final String id;
    private final String nombre;
    private final String descripcion;
    private final double danioBase;

    /**
     * Constructor del poder
     * @param id
     * @param nombre
     * @param descripcion
     * @param danioBase
     */
    protected Poder(String id, String nombre, String descripcion, double danioBase) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.danioBase = danioBase;
    }

    // Metodos getter
    public String getId() { 
        return id; 
    }
    public String getNombre() { 
        return nombre; 
    }
    public String getDescripcion() { 
        return descripcion; 
    }
    public double getDanioBase() { 
        return danioBase; 
    }

    /**
     * MEtodo que crea el efecto que el poder le pone al rival
     * @return
     */
    public abstract Efecto crearEfecto();

    /**
     * Metodo que nos dice la velocidad del tiro px/s
     * @return
     */
    protected abstract double getVelocidadTiro();

    /**
     * Metodo que nos da la pendiente de tiro (horizontal = 0)
     * @return
     */
    protected double getInclinacion() { 
        return Reglas.INCLINACION_PODER; 
    }
    /**
     * Metodo que nos da el factor que afecta la gravedad al balon con el poder (normal = 1)
     * @return
     */
    public double getFactorGravedad() { 
        return Reglas.FACTOR_GRAVEDAD_PODER; 
    }
    /**
     * Metodo que nos da el factor de rebote
     * @return
     */
    public double getReboteSuelo() { 
        return Reglas.REBOTE_SUELO; 
    }
    /**
     * metodo que nos ddice si el tiro sigue activo despues de rebotar en el suelo (el tornado si)
     * @return
     */
    public boolean sobreviveRebotesEnSuelo() { 
        return false; 
    }
    /**
     * Metodo que nos permite ajustar el balon antes de su lanzamiento
     * @param balon
     */
    protected void prepararBalon(Balon balon) { }

    /**
     * Metodo que lanza el balon con el poder y hacia el rival
     * @param balon
     * @param autor
     */
    public void lanzar(Balon balon, Jugador autor) {
        //ajustamos el balon para el tiro
        prepararBalon(balon);
        double dx = autor.getDireccion();
        double dy = getInclinacion();
        // normalizamos la direccion final
        double n = Math.hypot(dx, dy);
        double v = getVelocidadTiro();
        balon.lanzarConPoder(this, autor, dx / n * v, dy / n * v);
    }

    /**
     * Metodo que calcula el dannio de su poder (dannioTiro * multiplicadorKick)
     * @param autor
     * @return
     */
    public double calcularDanio(Jugador autor) {
        return danioBase * autor.getEstadisticas().getMultiplicadorKick();
    }

    /**
     * MEtodo que aplica el dannio y efecto cuando el balon golpea al rival
     * @param victima
     * @param autor
     * @param partida
     * @return
     */
    public Efecto aplicarA(Jugador victima, Jugador autor, Partida partida) {
        victima.recibirDanio(calcularDanio(autor));
        Efecto e = crearEfecto();
        victima.agregarEfecto(e, partida);
        return e;
    }

    @Override
    public String toString() { 
        return nombre; 
    }
}

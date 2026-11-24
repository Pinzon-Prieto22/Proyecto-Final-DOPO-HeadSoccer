package dominio;

/**
 * Clase Pie
 * Responsable del pie del jugador
 * 
 * es Serializable para poder guardarlo y abrirlo
 * 
 * @author Bryan Pinzon y Valentina Prieto
 * @version Entrega Final Proyecto Final
 */

public class Pie implements java.io.Serializable {
    // identificador de serializacion
    private static final long serialVersionUID = 1L;
    private boolean activo;
    private double tiempo;
    private double enfriamiento;
    private boolean golpeoBalon;
    private boolean golpeoRival;

    /**
     * Metodo que se encarga de iniciar una patada
     * @return true si puede
     */
    public boolean iniciar() {
        if (activo || enfriamiento > 0) return false;
        activo = true; 
        tiempo = 0; 
        golpeoBalon = false; 
        golpeoRival = false;
        return true;
    }

    /**
     * Metodo que actualiza el pie respecto al tiempo
     * @param dt
     */
    public void actualizar(double dt) {
        if (activo) {
            tiempo += dt;
            if (tiempo >= Reglas.DURACION_PATADA) { 
                activo = false; 
                enfriamiento = Reglas.ENFRIAMIENTO_PATADA; 
            }
        } else if (enfriamiento > 0) {
            enfriamiento -= dt;
        }
    }

    /**
     * MEtodo que cancela una patada 
     */
    public void cancelar(){ 
        activo = false; 
        tiempo = 0; 
        enfriamiento = 0; 
    }

    /**
     * Metodo que nos dice si el balon esta activo
     * @return
     */
    public boolean estaActivo(){ 
        return activo; 
    }
    
    public double getProgreso() { return Math.min(1, tiempo / Reglas.DURACION_PATADA); }

    /**
     * Metodo que nos da el angulo en radianes del pie respecto al jugador estando de frente
     * -80 esta abajo y 100 esta arriba
     * @return
     */
    public double getAngulo() {
        double g = Reglas.GRADOS_INICIO_PATADA + (Reglas.GRADOS_FIN_PATADA - Reglas.GRADOS_INICIO_PATADA) * getProgreso();
        return Math.toRadians(g);
    }

    /**
     * metodo que nos dice si se golpeo al balon
     * @return
     */
    public boolean yaGolpeoBalon(){ 
        return golpeoBalon; 
    }
    /**
     * Metodo que marca el golpe del balon
     */
    public void marcarGolpeoBalon(){ 
        golpeoBalon = true; 
    }
    /**
     * MEtodo que nos dice si golpeo rival
     * @return
     */
    public boolean yaGolpeoRival(){ 
        return golpeoRival; 
    }
    /**
     * Metodo que marca el colpear rival
     */
    public void marcarGolpeoRival(){ 
        golpeoRival = true; 
    }
}

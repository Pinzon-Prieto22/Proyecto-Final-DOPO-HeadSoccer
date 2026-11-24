package dominio;

import java.io.Serializable;

/**
 * Clase Abstracta Entidad
 * Responsable de los entes que cumplen o tienen:
 * una posicion y una velocidad en la cancha (balon y jugadores)
 * 
 * es Serializable para poder guardarlos y abrirlos
 * 
 * @author Bryan Pinzon y Valentina Prieto
 * @version Entrega Final Proyecto Final
 */

public abstract class Entidad implements Serializable {
    // identificador de serializacion
    private static final long serialVersionUID = 1L;
    // posiciones
    protected double x;
    protected double y;
    // velociadades
    protected double vx;
    protected double vy;

    // Metodos getter
    public double getX(){ 
        return x; 
    }
    public double getY(){ 
        return y; 
    }
    public double getVx(){ 
        return vx; 
    }
    public double getVy(){ 
        return vy; 
    }

    //Metodos setter
    public void setPosicion(double x, double y) { 
        this.x = x; 
        this.y = y; 
    }
    public void setVelocidad(double vx, double vy){ 
        this.vx = vx; 
        this.vy = vy; 
    }
    /**
     * Metodo para desplazarse en x
     * @param dx distancia a desplazarse
     */
    public void desplazarX(double dx){ 
        this.x += dx; 
    }

    /**
     * Metodo que actualiza/avanza la entidad 
     * @param dt segundos
     * @param cancha la cancha
     */
    public abstract void actualizar(double dt, Cancha cancha);
}

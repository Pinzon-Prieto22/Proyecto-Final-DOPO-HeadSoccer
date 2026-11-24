package dominio;

/**
 * Clase abstracta que representa una entidad
 * que una entidad es lo que tenga posicion y velocidad
 */
public abstract class Entidad {
    protected double x;
    protected double y;
    protected double vx;
    protected double vy;

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

    public void setPosicion(double x, double y) { 
        this.x = x; 
        this.y = y; 
    }
    public void setVelocidad(double vx, double vy) { 
        this.vx = vx; 
        this.vy = vy; 
    }
    public void desplazarX(double dx){ 
        this.x += dx; 
    }
    public abstract void actualizar(double dt, Cancha cancha);
}

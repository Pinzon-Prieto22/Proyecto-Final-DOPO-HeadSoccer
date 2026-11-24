package dominio;

/**
 * Clase Circulo
 * Responsable de lo que compone el circulo y el calculo de choque
 * 
 * @author Bryan Pinzon y Valentina Prieto
 * @version Entrega Final Proyecto Final
 */

public final class Circulo {
    // atributos
    private final double x;
    private final double y;
    private final double radio;

    /**
     * Constructor
     * @param x
     * @param y
     * @param radio
     */
    public Circulo(double x, double y, double radio) {
        this.x = x;
        this.y = y;
        this.radio = radio;
    }

    // Metodos getter
    public double getX(){ 
        return x; 
    }
    public double getY(){ 
        return y; 
    }
    public double getRadio(){ 
        return radio; 
    }

    /**
     * Metodo que nos ayuda a calcular si el circulo se interseca con otro
     * 
     * @param otro que es el otro circulo con el que queremos saber si se interseca
     * @return true si si se intersecan
     */
    public boolean intersecta(Circulo otro) {
        // calculamos la diferencia de los centros respecto a x
        double dx = x - otro.x;
        // calculamos la diferencia de los centros respecto a y
        double  dy = y - otro.y;
        // sumamos los radios
        double r = radio + otro.radio;
        // vemos si la suma de sus radios es mayor que su distancia central (se intersecan)
        return dx * dx + dy * dy < r * r;
    }
}

package dominio;

/**
 * Clase Arco
 * Responsable del arco/s de la cancha y de saber si se hace gol o no
 * 
 * es Serializable para poder guardarlo y abrirlo
 * 
 * @author Bryan Pinzon y Valentina Prieto
 * @version Entrega Final Proyecto Final
 */

public class Arco implements java.io.Serializable {
    // identificador de serializacion
    private static final long serialVersionUID = 1L;
    private final Lado lado;
    private final double x;

    /**
     * Constructor del arco en su respectivo lado
     */
    public Arco(Lado lado) {
        this.lado = lado;
        this.x = lado == Lado.IZQUIERDO ? 0 : Reglas.ANCHO - Reglas.ARCO_ANCHO;
    }

    // Metodos getter
    public Lado getLado(){ 
        return lado; 
    }
    public double getX(){ 
        return x; 
    }
    public double getAncho(){ 
        return Reglas.ARCO_ANCHO; 
    }
    public double getYTravesano(){ 
        return Reglas.Y_SUELO - Reglas.ARCO_ALTO; 
    }

    /**
     * Metodo que nos dice si el balon se encuentra completamente dentrio del arco
     * @param b
     * @return
     */
    public boolean contieneCompleto(Balon b) {
        double r = b.getRadio();
        // calculamos que este dentro de las dimensiones del arco
        boolean dentroX = lado == Lado.IZQUIERDO ? b.getX() + r <= x + Reglas.ARCO_ANCHO: b.getX() - r >= x;
        boolean bajoElTravesano = b.getY() - r >= getYTravesano() + Reglas.GROSOR_TRAVESANO;
        // si lo cumple estra dentro
        return dentroX && bajoElTravesano;
    }
}

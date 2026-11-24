package dominio;

/**
 * Clase Cancha
 * Responsable de las dimesniones de la cancha y los arcos
 * 
 * es Serializable para poder guardarlo y abrirlo
 * 
 * @author Bryan Pinzon y Valentina Prieto
 * @version Entrega Final Proyecto Final
 */

public class Cancha implements java.io.Serializable {
    // identificador de serializacion
    private static final long serialVersionUID = 1L;
    //arcos en cada lado
    private final Arco izquierdo = new Arco(Lado.IZQUIERDO);
    private final Arco derecho = new Arco(Lado.DERECHO);

    // Metodos getter
    public double getAncho(){ 
        return Reglas.ANCHO; 
    }
    public double getAlto(){ 
        return Reglas.ALTO; 
    }
    public double getYSuelo(){ 
        return Reglas.Y_SUELO; 
    }
    public double getCentroX(){ 
        return Reglas.ANCHO / 2; 
    }
    public Arco getArco(Lado lado){
        return lado == Lado.IZQUIERDO ? izquierdo : derecho;
    }

    /**
     * Metodo que nos da la posicion inicial horizontal de un jugador respecto a su cancha
     * @param lado
     * @return
     */
    public static double posicionInicialX(Lado lado) {
        return lado == Lado.IZQUIERDO ? Reglas.ANCHO * 0.25 : Reglas.ANCHO * 0.75;
    }
}

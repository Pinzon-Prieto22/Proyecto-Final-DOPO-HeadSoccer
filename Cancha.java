package dominio;

/**
 * Clase que representa toda la cancga
 */
public class Cancha {
    private final Arco izquierdo = new Arco(Lado.IZQUIERDO);
    private final Arco derecho = new Arco(Lado.DERECHO);

    public double getAncho() { 
        return Reglas.ANCHO; 
    }
    public double getAlto() { 
        return Reglas.ALTO; 
    }
    public double getYSuelo() { 
        return Reglas.Y_SUELO; 
    }
    public double getCentroX() { 
        return Reglas.ANCHO / 2; 
    }

    public Arco getArco(Lado lado) {
        return lado == Lado.IZQUIERDO ? izquierdo : derecho;
    }

    public static double posicionInicialX(Lado lado) {
        return lado == Lado.IZQUIERDO ? Reglas.ANCHO * 0.25 : Reglas.ANCHO * 0.75;
    }
}

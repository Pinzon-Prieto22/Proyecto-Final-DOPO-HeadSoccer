package dominio;

public class Arco {
    private final Lado lado;
    private final double x;

    public Arco(Lado lado) {
        this.lado = lado;
        this.x = lado == Lado.IZQUIERDO ? 0 : Reglas.ANCHO - Reglas.ARCO_ANCHO;
    }

    public Lado getLado() { 
        return lado; 
    }
    public double getX() { 
        return x; 
    }
    public double getAncho() { 
        return Reglas.ARCO_ANCHO; 
    }
    public double getYTravesano() { 
        return Reglas.Y_SUELO - Reglas.ARCO_ALTO; 
    }

    public boolean contieneCompleto(Balon b) {
        double r = b.getRadio();
        boolean dentroX = lado == Lado.IZQUIERDO
                ? b.getX() + r <= x + Reglas.ARCO_ANCHO
                : b.getX() - r >= x;
        boolean bajoElTravesano = b.getY() - r >= getYTravesano() + Reglas.GROSOR_TRAVESANO;
        return dentroX && bajoElTravesano;
    }
}

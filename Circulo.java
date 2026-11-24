package dominio;

public final class Circulo {
    private final double x;
    private final double y;
    private final double radio;

    public Circulo(double x, double y, double radio){
        this.x = x;
        this.y = y;
        this.radio = radio;
    }

    public double getX(){ 
        return x; 
    }
    public double getY() { 
        return y; 
    }
    public double getRadio() { 
        return radio; 
    }

    public boolean intersecta(Circulo otro){
        double dx = x - otro.x, dy = y - otro.y, r = radio + otro.radio;
        return dx * dx + dy * dy < r * r;
    }
}

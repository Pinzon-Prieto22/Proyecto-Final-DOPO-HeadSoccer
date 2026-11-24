package presentacion;

/**
 * enum donde guardamos las posibles resoluciones qye ofrece el juego
 */
public enum Resolucion {
    PEQUENA("Pequeña", 800, 450),
    MEDIANA("Mediana", 1024, 576),
    GRANDE("Grande", 1280, 720);

    private final String nombre;
    private final int ancho, alto;

    Resolucion(String nombre, int ancho, int alto) {
        this.nombre = nombre;
        this.ancho = ancho;
        this.alto = alto;
    }

    public int getAncho() { return ancho; }
    public int getAlto() { return alto; }

    @Override
    public String toString() { return nombre + " (" + ancho + " \u00d7 " + alto + ")"; }
}

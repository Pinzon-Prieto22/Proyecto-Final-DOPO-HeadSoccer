package dominio;

/**
 * Clase responsable del marcador
 */
public class Marcador {
    private final int[] goles = new int[2];

    public void sumarGol(Lado anotador) { 
        goles[anotador.ordinal()]++; 
    }
    public int getGoles(Lado lado) { 
        return goles[lado.ordinal()]; 
    }
}

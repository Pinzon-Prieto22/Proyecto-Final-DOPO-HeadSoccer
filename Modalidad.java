package dominio;

/**
 * Enum en el que guardamos las modalidades que ofrece el juego
 */
public enum Modalidad {
    PVP("Jugador vs jugador", true, true);

    private final String nombre;
    private final boolean humanoIzquierdo;
    private final boolean humanoDerecho;

    Modalidad(String nombre, boolean humanoIzquierdo, boolean humanoDerecho) {
        this.nombre = nombre;
        this.humanoIzquierdo = humanoIzquierdo;
        this.humanoDerecho = humanoDerecho;
    }

    public String getNombre() { 
        return nombre; 
    }

    public boolean esHumano(Lado lado) { 
        return lado == Lado.IZQUIERDO ? humanoIzquierdo : humanoDerecho; 
    }

    @Override
    public String toString() { 
        return nombre; 
    }
}

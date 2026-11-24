package dominio;

/**
 * Clase responsable del personaje
 */
public class Personaje {
    private final String nombre;
    private final Equipo equipo;
    private final Poder poder;

    public Personaje(String nombre, Equipo equipo, Poder poder){
        this.nombre = nombre;
        this.equipo = equipo;
        this.poder = poder;
    }

    public String getId() { 
        return equipo.getCodigo().toLowerCase(); 
    }
    public String getNombre(){ 
        return nombre; 
    }
    public Equipo getEquipo() { 
        return equipo; 
    }
    public Poder getPoder(){ 
        return poder; 
    }
    @Override
    public String toString(){ 
        return nombre + " (" + equipo.getPais() + ")"; 
    }
}

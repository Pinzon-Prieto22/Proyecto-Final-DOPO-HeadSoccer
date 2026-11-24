package dominio;

import java.io.Serializable;

/**
 * Clase Personaje
 * Responsable de todos los datos y todo lo del personaje
 * 
 * es Serializable para poder guardarlo y abrirlo
 * 
 * @author Bryan Pinzon y Valentina Prieto
 * @version Entrega Final Proyecto Final
 */

public class Personaje implements Serializable {
    //Identificador de serializacion
    private static final long serialVersionUID = 1L;
    private final String nombre;
    private final Equipo equipo;
    private final Poder poder;

    /**
     * Constructor del personaje
     * @param nombre
     * @param equipo
     * @param poder
     */
    public Personaje(String nombre, Equipo equipo, Poder poder) {
        this.nombre = nombre;
        this.equipo = equipo;
        this.poder = poder;
    }

    //MEtodos getter
    public String getId() { 
        return equipo.getCodigo().toLowerCase(); 
    }
    public String getNombre() { 
        return nombre; 
    }
    public Equipo getEquipo() { 
        return equipo; 
    }
    public Poder getPoder() { 
        return poder; 
    }

    @Override
    public String toString() { 
        return nombre + " (" + equipo.getPais() + ")";
    }
}

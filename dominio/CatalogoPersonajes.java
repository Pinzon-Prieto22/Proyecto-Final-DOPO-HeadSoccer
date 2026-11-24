package dominio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Clase CatalogoPersonajes
 * Responsable del registro de los personajes que tenemos
 * Para agregar uno lo ponemos en una linea
 * 
 * @author Bryan Pinzon y Valentina Prieto
 * @version Entrega Final Proyecto Final
 */
public final class CatalogoPersonajes {
    private static final CatalogoPersonajes INSTANCIA = new CatalogoPersonajes();
    private final List<Personaje> personajes = new ArrayList<Personaje>();

    /**
     * Constructor del catalogo
     */
    private CatalogoPersonajes() { 
        registrarPorDefecto(); 
    }

    /**
     * Metodo que nos da el catalogo
     * @return
     */
    public static CatalogoPersonajes getInstancia() { 
        return INSTANCIA; 
    }

    /**
     * Metodo que registra todos nuestros personajes disponibles
     */
    private void registrarPorDefecto() {
        registrar(new Personaje("Min-jun", new Equipo("KOR", "Corea del Sur"), new PoderAuraAzul()));
        registrar(new Personaje("Samuel", new Equipo("CMR", "Camerún"), new PoderRayo()));
        registrar(new Personaje("Chidi", new Equipo("NGA", "Nigeria"), new PoderTornado()));
        registrar(new Personaje("Jake", new Equipo("USA", "Estados Unidos"), new PoderIlusion()));
    }

    /**
     * Metodo que registra un personaje, si ya existe uno con ese id lo reemplaza
     * @param p
     */
    public void registrar(Personaje p) {
        for (int i = 0; i < personajes.size(); i++) {
            if (personajes.get(i).getId().equals(p.getId())) { 
                personajes.set(i, p); return; 
            }
        }
        personajes.add(p);
    }

    /**
     * Metodo que nos dice todos los personajes disponibles
     * @return
     */
    public List<Personaje> getTodos() { 
        return Collections.unmodifiableList(personajes); 
    }

    /**
     * MEtodo que busca un pj por su id
     * @param id
     * @return
     */
    public Personaje buscar(String id) {
        for (Personaje p : personajes) if (p.getId().equals(id)) return p;
        return null;
    }
}

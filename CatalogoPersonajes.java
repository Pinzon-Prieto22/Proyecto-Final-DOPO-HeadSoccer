package dominio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Clase que se encarga del registro de todos los personajes
 */
public final class CatalogoPersonajes {
    private static final CatalogoPersonajes INSTANCIA = new CatalogoPersonajes();
    private final List<Personaje> personajes = new ArrayList<Personaje>();

    private CatalogoPersonajes() { 
        registrarPorDefecto();
    }

    public static CatalogoPersonajes getInstancia() { 
        return INSTANCIA; 
    }

    private void registrarPorDefecto() {
        registrar(new Personaje("Min-jun", new Equipo("KOR", "Corea del Sur"), new PoderAuraAzul()));
        registrar(new Personaje("Samuel", new Equipo("CMR", "Camerún"), new PoderRayo()));
        registrar(new Personaje("Chidi", new Equipo("NGA", "Nigeria"), new PoderTornado()));
        registrar(new Personaje("Jake", new Equipo("USA", "Estados Unidos"), new PoderIlusion()));
    }

    public void registrar(Personaje p){
        for (int i = 0; i < personajes.size(); i++) {
            if (personajes.get(i).getId().equals(p.getId())) { 
                personajes.set(i, p); return; 
            }
        }
        personajes.add(p);
    }

    public List<Personaje> getTodos(){ 
        return Collections.unmodifiableList(personajes); 
    }

    public Personaje buscar(String id) {
        for (Personaje p : personajes) if (p.getId().equals(id)) return p;
        return null;
    }
}

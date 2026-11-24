package dominio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Conjunto de perfiles de la aplicación: crea, busca, mejora y guarda el progreso de los jugadores. */
public class RegistroPerfiles {
    private final RepositorioPerfiles repositorio;
    private final Map<String, Perfil> perfiles = new LinkedHashMap<String, Perfil>();

    public RegistroPerfiles(RepositorioPerfiles repositorio) { this.repositorio = repositorio; }

    private static String clave(String nombre) { return nombre.trim().toLowerCase(Locale.ROOT); }

    public void cargar() throws DOPOSoccerException {
        perfiles.clear();
        for (Perfil p : repositorio.cargar()) perfiles.put(clave(p.getNombre()), p);
    }

    public void guardar() throws DOPOSoccerException { repositorio.guardar(perfiles.values()); }

    public Perfil crear(String nombre) throws DOPOSoccerException {
        Perfil p = new Perfil(nombre);
        if (perfiles.containsKey(clave(p.getNombre()))) {
            throw new DOPOSoccerException("Ya existe un perfil llamado \"" + p.getNombre() + "\".", "Elige otro nombre o selecciona ese perfil en la lista.");
        }
        perfiles.put(clave(p.getNombre()), p);
        try {
            guardar();
        } catch (DOPOSoccerException e) {
            perfiles.remove(clave(p.getNombre()));
            throw e;
        }
        return p;
    }

    public Perfil buscar(String nombre) { return nombre == null ? null : perfiles.get(clave(nombre)); }
    public boolean estaVacio() { return perfiles.isEmpty(); }
    public List<Perfil> getTodos() { return Collections.unmodifiableList(new ArrayList<Perfil>(perfiles.values())); }

    /** Compra una mejora para el perfil y guarda el progreso. */
    public void mejorar(Perfil perfil, TipoEstadistica tipo) throws DOPOSoccerException {
        perfil.comprarMejora(tipo);
        guardar();
    }

    /** Suma los puntos del partido a los perfiles humanos que jugaron (100 al ganador, 25 al perdedor). */
    public void registrarResultado(ResultadoPartida r) throws DOPOSoccerException {
        if (r.getGanador() == null) return;           // partida terminada sin ganador: no hay puntos
        boolean cambio = false;
        for (Lado lado : Lado.values()) {
            if (!r.esHumano(lado)) continue;
            Perfil p = buscar(r.getNombreJugador(lado));
            if (p == null) continue;
            p.registrarPartida(lado == r.getGanador(), r.getPuntosGanados(lado));
            cambio = true;
        }
        if (cambio) guardar();
    }
}

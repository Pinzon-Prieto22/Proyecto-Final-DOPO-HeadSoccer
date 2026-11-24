package dominio;

import java.util.Collection;
import java.util.List;

/** Dónde y cómo se guardan los perfiles (se puede cambiar el almacenamiento sin tocar el resto del juego). */
public interface RepositorioPerfiles {
    List<Perfil> cargar() throws DOPOSoccerException;
    void guardar(Collection<Perfil> perfiles) throws DOPOSoccerException;
}

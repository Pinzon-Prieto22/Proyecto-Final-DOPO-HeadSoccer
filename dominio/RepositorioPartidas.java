package dominio;

import java.io.File;

/** Guardar y abrir el estado de un partido. */
public interface RepositorioPartidas {
    void guardar(Partida partida, File archivo) throws DOPOSoccerException;
    Partida abrir(File archivo) throws DOPOSoccerException;
}

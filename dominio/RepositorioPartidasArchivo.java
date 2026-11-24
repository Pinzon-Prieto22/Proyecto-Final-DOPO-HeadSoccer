package dominio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

/** Guarda el partido completo con serialización de Java (extensión sugerida: .dopo). */
public class RepositorioPartidasArchivo implements RepositorioPartidas {
    public static final String EXTENSION = "dopo";
    private static final String MARCA = "DOPOSoccer-partida-v1";

    @Override
    public void guardar(Partida partida, File archivo) throws DOPOSoccerException {
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(archivo));
            out.writeUTF(MARCA);
            out.writeObject(partida);
        } catch (IOException e) {
            Bitacora.error("No se pudo guardar la partida en " + archivo.getAbsolutePath(), e);
            throw new DOPOSoccerException("No se pudo guardar la partida en \"" + archivo.getName() + "\".",
                    "Revisa que la carpeta exista, que tengas permisos de escritura y que haya espacio en disco.", e);
        } finally {
            cerrar(out);
        }
    }

    @Override
    public Partida abrir(File archivo) throws DOPOSoccerException {
        if (!archivo.exists()) {
            throw new DOPOSoccerException("El archivo \"" + archivo.getName() + "\" no existe.", "Elige un archivo de partida guardado (.dopo).");
        }
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(archivo));
            if (!MARCA.equals(in.readUTF())) throw new StreamCorruptedException("marca incorrecta");
            Object o = in.readObject();
            if (!(o instanceof Partida)) throw new StreamCorruptedException("el archivo no contiene una partida");
            return (Partida) o;
        } catch (InvalidClassException e) {
            Bitacora.error("Partida de otra versión: " + archivo.getAbsolutePath(), e);
            throw new DOPOSoccerException("La partida \"" + archivo.getName() + "\" se guardó con otra versión del juego y no se puede abrir.",
                    "Abre una partida guardada con esta misma versión.", e);
        } catch (IOException | ClassNotFoundException e) {
            Bitacora.error("No se pudo abrir la partida " + archivo.getAbsolutePath(), e);
            throw new DOPOSoccerException("No se pudo abrir \"" + archivo.getName() + "\": no es una partida válida de DOPOSoccer o está dañada.",
                    "Elige otro archivo (.dopo) o vuelve a guardar la partida.", e);
        } finally {
            cerrar(in);
        }
    }

    private static void cerrar(java.io.Closeable c) {
        if (c != null) try { c.close(); } catch (IOException e) { /* nada */ }
    }
}

package dominio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/** Guarda los perfiles en un archivo (serialización de Java): datos/perfiles.dat */
public class RepositorioPerfilesArchivo implements RepositorioPerfiles {
    private final File archivo;

    public RepositorioPerfilesArchivo() { this(new File("datos" + File.separator + "perfiles.dat")); }
    public RepositorioPerfilesArchivo(File archivo) { this.archivo = archivo; }

    @Override
    @SuppressWarnings("unchecked")
    public List<Perfil> cargar() throws DOPOSoccerException {
        if (!archivo.exists()) return new ArrayList<Perfil>();
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(archivo));
            return new ArrayList<Perfil>((List<Perfil>) in.readObject());
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            Bitacora.error("No se pudo leer " + archivo.getAbsolutePath(), e);
            File respaldo = new File(archivo.getPath() + ".danado");
            try { Files.copy(archivo.toPath(), respaldo.toPath(), StandardCopyOption.REPLACE_EXISTING); } catch (IOException ignorado) { /* sin respaldo */ }
            throw new DOPOSoccerException("No se pudieron cargar los perfiles: el archivo está dañado o no es compatible.",
                    "Se guardó una copia en " + respaldo.getName() + ". Si continúas, se empezará sin perfiles.", e);
        } finally {
            cerrar(in);
        }
    }

    @Override
    public void guardar(Collection<Perfil> perfiles) throws DOPOSoccerException {
        File dir = archivo.getAbsoluteFile().getParentFile();
        if (dir != null && !dir.exists() && !dir.mkdirs()) {
            throw new DOPOSoccerException("No se pudo crear la carpeta " + dir + " para guardar los perfiles.",
                    "Revisa que tengas permisos de escritura en la carpeta del juego.");
        }
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(archivo));
            out.writeObject(new ArrayList<Perfil>(perfiles));
        } catch (IOException e) {
            Bitacora.error("No se pudo escribir " + archivo.getAbsolutePath(), e);
            throw new DOPOSoccerException("No se pudieron guardar los perfiles en " + archivo.getName() + ".",
                    "Revisa el espacio en disco y los permisos de la carpeta del juego.", e);
        } finally {
            cerrar(out);
        }
    }

    private static void cerrar(java.io.Closeable c) {
        if (c != null) try { c.close(); } catch (IOException e) { /* nada */ }
    }
}

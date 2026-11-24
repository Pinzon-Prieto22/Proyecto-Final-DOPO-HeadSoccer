package presentacion;

import java.awt.Component;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import dominio.RepositorioPartidasArchivo;

/** Diálogos para elegir el archivo donde guardar o desde donde abrir una partida (carpeta "partidas"). */
public final class SelectorArchivo {
    private SelectorArchivo() { }

    private static JFileChooser crear() {
        File dir = new File("partidas");
        if (!dir.exists()) dir.mkdirs();
        JFileChooser fc = new JFileChooser(dir);
        fc.setFileFilter(new FileNameExtensionFilter("Partidas de DOPOSoccer (*." + RepositorioPartidasArchivo.EXTENSION + ")", RepositorioPartidasArchivo.EXTENSION));
        return fc;
    }

    /** @return el archivo elegido o null si el usuario canceló */
    public static File abrir(Component padre) {
        JFileChooser fc = crear();
        fc.setDialogTitle("Abrir partida");
        return fc.showOpenDialog(padre) == JFileChooser.APPROVE_OPTION ? fc.getSelectedFile() : null;
    }

    /** @return el archivo elegido (con extensión .dopo) o null si el usuario canceló o no quiso sobrescribir */
    public static File guardar(Component padre) {
        JFileChooser fc = crear();
        fc.setDialogTitle("Guardar partida");
        if (fc.showSaveDialog(padre) != JFileChooser.APPROVE_OPTION) return null;
        File f = fc.getSelectedFile();
        String ext = "." + RepositorioPartidasArchivo.EXTENSION;
        if (!f.getName().toLowerCase().endsWith(ext)) f = new File(f.getParentFile(), f.getName() + ext);
        if (f.exists() && !Mensajes.confirmar(padre, "Sobrescribir", "El archivo \"" + f.getName() + "\" ya existe. \u00bfQuieres sobrescribirlo?")) return null;
        return f;
    }
}

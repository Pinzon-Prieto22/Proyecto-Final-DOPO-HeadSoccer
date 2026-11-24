package dominio;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase responsable del log de errores
 */
public final class Bitacora {
    private static final File ARCHIVO = new File("logs" + File.separator + "dopoSoccer.log");
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Bitacora() { }

    public static File getArchivo() { return ARCHIVO; }

    /** Registra un error con su contexto y su traza completa. */
    public static synchronized void error(String contexto, Throwable causa) {
        escribir("ERROR", contexto, causa);
    }

    public static synchronized void info(String mensaje) {
        escribir("INFO ", mensaje, null);
    }

    private static void escribir(String nivel, String mensaje, Throwable causa) {
        try {
            File dir = ARCHIVO.getParentFile();
            if (dir != null && !dir.exists()) dir.mkdirs();
            PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO, true));
            try {
                pw.println(LocalDateTime.now().format(FORMATO) + " [" + nivel + "] " + mensaje);
                if (causa != null) causa.printStackTrace(pw);
            } finally {
                pw.close();
            }
        } catch (IOException e) {
            System.err.println("No se pudo escribir en la bitácora: " + e.getMessage());
        }
    }
}

package dominio;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase Bitacora
 * Responsable del log de errores 
 * escribe en logs/dopoSoccer.log
 * 
 * @author Bryan Pinzon y Valentina Prieto
 * @version Entrega Final Proyecto Final
 */

public final class Bitacora {
    private static final File ARCHIVO = new File("logs" + File.separator + "dopoSoccer.log");
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * constructor
     */
    private Bitacora() { }

    /**
     * Metodo que nos da el archivo donde se guardan los logs
     * @return
     */
    public static File getArchivo() { 
        return ARCHIVO; 
    }

    /**
     * Metodo que registra el error, el contraxto y su causa
     * @param contexto
     * @param causa
     */
    public static synchronized void error(String contexto, Throwable causa) {
        escribir("ERROR", contexto, causa);
    }

    /**
     * MEtodo que registra informacion sobre lo que ocurre
     * @param mensaje
     */
    public static synchronized void info(String mensaje) {
        escribir("INFO ", mensaje, null);
    }

    /**
     * Metoso que escribe sobre el archivo 
     * @param nivel si es info o si es error
     * @param mensaje
     * @param causa
     */
    private static void escribir(String nivel, String mensaje, Throwable causa) {
        try {
            File dir = ARCHIVO.getParentFile(); // obtenemos la carpeta del archivo
            if (dir != null && !dir.exists()) dir.mkdirs(); // si no existe la creamos
            PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO, true)); // abrimos el archivo
            try {
                pw.println(LocalDateTime.now().format(FORMATO) + " [" + nivel + "] " + mensaje); // escribimos los detalles
                if (causa != null) causa.printStackTrace(pw); // para obtener su info tecnica
            } finally {
                pw.close(); // cerramos el archivo
            }
        } catch (IOException e) {
            System.err.println("No se pudo escribir en la bitacora: " + e.getMessage()); 
        }
    }
}

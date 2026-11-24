package presentacion;

import java.awt.Component;
import javax.swing.JOptionPane;
import dominio.Bitacora;
import dominio.DOPOSoccerException;

public final class Mensajes {
    private Mensajes() { }

    private static String html(String s) {
        return s == null ? "" : s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    public static void error(Component padre, String titulo, DOPOSoccerException e) {
        StringBuilder sb = new StringBuilder("<html><body style='width:400px'><b>").append(html(e.getMessage())).append("</b>");
        if (e.getSugerencia() != null) sb.append("<br><br>\u00bfQu\u00e9 puedes hacer? ").append(html(e.getSugerencia()));
        if (e.getCause() != null) {
            sb.append("<br><br><font color='gray'>Detalle t\u00e9cnico: ").append(html(e.getCause().getClass().getSimpleName()))
              .append(": ").append(html(e.getCause().getMessage())).append("<br>Registrado en ")
              .append(html(Bitacora.getArchivo().getPath())).append("</font>");
        }
        JOptionPane.showMessageDialog(padre, sb.toString(), titulo, JOptionPane.ERROR_MESSAGE);
    }

    public static void inesperado(Component padre, Throwable t) {
        Bitacora.error("Error inesperado", t);
        String msg = "<html><body style='width:400px'><b>Ocurri\u00f3 un error inesperado.</b><br><br>"
                + html(t.getClass().getSimpleName() + ": " + t.getMessage())
                + "<br><br>Puedes intentar continuar. Si el problema se repite, avisa a los desarrolladores y env\u00edales el archivo "
                + html(Bitacora.getArchivo().getPath()) + ".</body></html>";
        JOptionPane.showMessageDialog(padre, msg, "Error inesperado", JOptionPane.ERROR_MESSAGE);
    }

    public static void info(Component padre, String titulo, String mensaje) {
        JOptionPane.showMessageDialog(padre, "<html><body style='width:380px'>" + html(mensaje) + "</body></html>", titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean confirmar(Component padre, String titulo, String pregunta) {
        return JOptionPane.showConfirmDialog(padre, "<html><body style='width:380px'>" + html(pregunta) + "</body></html>",
                titulo, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }
}

package dominio;

import java.io.Serializable;
import java.util.regex.Pattern;

/** Perfil de un jugador humano: nombre, puntos acumulados y estadísticas. Es el progreso que se conserva entre sesiones. */
public class Perfil implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Pattern NOMBRE_VALIDO = Pattern.compile("[\\p{L}0-9 _-]{1,16}");

    private final String nombre;
    private final Estadisticas estadisticas = new Estadisticas();
    private int puntos;
    private int victorias;
    private int derrotas;

    public Perfil(String nombre) throws DOPOSoccerException {
        this.nombre = validarNombre(nombre);
    }

    public static String validarNombre(String nombre) throws DOPOSoccerException {
        String n = nombre == null ? "" : nombre.trim();
        if (!NOMBRE_VALIDO.matcher(n).matches()) {
            throw new DOPOSoccerException("El nombre del perfil \"" + (nombre == null ? "" : nombre) + "\" no es válido.",
                    "Usa entre 1 y 16 caracteres: letras, números, espacios, guion o guion bajo.");
        }
        return n;
    }

    /**
     * Compra una mejora: descuenta el precio de los puntos y sube la estadística un nivel.
     * @throws DOPOSoccerException si ya está en el máximo o no alcanzan los puntos
     */
    public void comprarMejora(TipoEstadistica tipo) throws DOPOSoccerException {
        if (estadisticas.estaEnMaximo(tipo)) {
            throw new DOPOSoccerException(tipo.getNombre() + " ya está en el nivel máximo (" + Reglas.NIVEL_MAXIMO + ").",
                    "Elige otra estadística para mejorar.");
        }
        int precio = estadisticas.getPrecioSiguiente(tipo);
        if (puntos < precio) {
            throw new DOPOSoccerException("No tienes puntos suficientes para mejorar " + tipo.getNombre()
                    + ": cuesta " + precio + " y tienes " + puntos + ".",
                    "Gana más partidos: la victoria da " + Reglas.PUNTOS_VICTORIA + " puntos y la derrota " + Reglas.PUNTOS_DERROTA + ".");
        }
        puntos -= precio;
        estadisticas.subir(tipo);
    }

    /** Suma los puntos de un partido terminado y actualiza el historial. */
    public void registrarPartida(boolean victoria, int puntosGanados) {
        puntos += puntosGanados;
        if (victoria) victorias++; else derrotas++;
    }

    public String getNombre() { return nombre; }
    public int getPuntos() { return puntos; }
    public Estadisticas getEstadisticas() { return estadisticas; }
    public int getVictorias() { return victorias; }
    public int getDerrotas() { return derrotas; }

    @Override
    public String toString() { return nombre; }
}

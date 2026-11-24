package dominio;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Estadísticas de un jugador (aplican a todos los personajes que use). Todas empiezan en 1 y llegan a 10.
 * Cada punto por encima de 1 suma un 10 % sobre el valor base.
 */
public class Estadisticas implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int[] niveles = new int[TipoEstadistica.values().length];

    public Estadisticas() { Arrays.fill(niveles, Reglas.NIVEL_MINIMO); }

    /** Copia (los partidos trabajan con una copia de las estadísticas del perfil). */
    public Estadisticas(Estadisticas otra) { System.arraycopy(otra.niveles, 0, niveles, 0, niveles.length); }

    public int getNivel(TipoEstadistica t) { return niveles[t.ordinal()]; }
    public boolean estaEnMaximo(TipoEstadistica t) { return getNivel(t) >= Reglas.NIVEL_MAXIMO; }

    /** Precio de subir esta estadística al siguiente nivel: 500, 1000, 2000... (por estadística). -1 si ya está en el máximo. */
    public int getPrecioSiguiente(TipoEstadistica t) {
        if (estaEnMaximo(t)) return -1;
        return Reglas.PRECIO_BASE_MEJORA * (1 << (getNivel(t) - Reglas.NIVEL_MINIMO));
    }

    /** Sube un nivel (el cobro lo controla Perfil). */
    void subir(TipoEstadistica t) {
        if (estaEnMaximo(t)) throw new IllegalStateException(t + " ya está en el máximo");
        niveles[t.ordinal()]++;
    }

    private double factor(TipoEstadistica t) { return 1 + Reglas.INCREMENTO_POR_NIVEL * (getNivel(t) - Reglas.NIVEL_MINIMO); }

    /** Velocidad de desplazamiento (px/s). */
    public double getVelocidad() { return Reglas.VELOCIDAD_BASE * factor(TipoEstadistica.SPEED); }

    /** Velocidad inicial del salto: la ALTURA crece 10 % por nivel (la altura depende del cuadrado de la velocidad). */
    public double getImpulsoSalto() { return Reglas.IMPULSO_SALTO_BASE * Math.sqrt(factor(TipoEstadistica.JUMP)); }

    /** multiplicadorKick = 1 + 0,10 * (Kick - 1). */
    public double getMultiplicadorKick() { return factor(TipoEstadistica.KICK); }

    /** vidaMax = 100 * (1 + 0,10 * (Survival - 1)). */
    public double getVidaMaxima() { return Reglas.VIDA_BASE * factor(TipoEstadistica.SURVIVAL); }

    /** Daño de una patada normal: 10 * multiplicadorKick. */
    public double getDanioPatada() { return Reglas.DANIO_PATADA_BASE * getMultiplicadorKick(); }
}

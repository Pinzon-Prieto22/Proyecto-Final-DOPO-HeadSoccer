package dominio;

import java.util.Arrays;

/**
 * Clase responsable de las estadisticas de un jugador
 */
public class Estadisticas {
    private final int[] niveles = new int[TipoEstadistica.values().length];

    public Estadisticas() { 
        Arrays.fill(niveles, Reglas.NIVEL_MINIMO); 
    }

    public Estadisticas(Estadisticas otra) { 
        System.arraycopy(otra.niveles, 0, niveles, 0, niveles.length); 
    }

    public int getNivel(TipoEstadistica t) { 
        return niveles[t.ordinal()]; 
    }
    public boolean estaEnMaximo(TipoEstadistica t) { 
        return getNivel(t) >= Reglas.NIVEL_MAXIMO; 
    }

    void subir(TipoEstadistica t) {
        if (estaEnMaximo(t)) throw new IllegalStateException(t + " ya está en el máximo");
        niveles[t.ordinal()]++;
    }

    private double factor(TipoEstadistica t) { 
        return 1 + Reglas.INCREMENTO_POR_NIVEL * (getNivel(t) - Reglas.NIVEL_MINIMO); 
    }

    public double getVelocidad() { 
        return Reglas.VELOCIDAD_BASE * factor(TipoEstadistica.SPEED); 
    }

    public double getImpulsoSalto() { 
        return Reglas.IMPULSO_SALTO_BASE * Math.sqrt(factor(TipoEstadistica.JUMP)); 
    }

    public double getMultiplicadorKick() { 
        return factor(TipoEstadistica.KICK); 
    }

    public double getVidaMaxima() { 
        return Reglas.VIDA_BASE * factor(TipoEstadistica.SURVIVAL); 
    }

    public double getDanioPatada() { 
        return Reglas.DANIO_PATADA_BASE * getMultiplicadorKick(); 
    }
}

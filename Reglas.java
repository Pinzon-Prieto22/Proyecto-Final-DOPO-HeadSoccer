package dominio;

/**
 * Clase responsable de las medidas globales del juego
 */

public final class Reglas {
    private Reglas() { }

    // generales
    public static final double ANCHO = 960;
    public static final double ALTO = 540;
    public static final double Y_SUELO = 470;
    // arco
    public static final double ARCO_ANCHO = 90;
    public static final double ARCO_ALTO = 170;
    public static final double GROSOR_TRAVESANO = 10;

    // tiempos en segundos
    public static final double PASO_FISICA = 1.0 / 120.0;
    public static final double DURACION_CUENTA_REGRESIVA = 3;
    public static final double DURACION_CELEBRACION = 2;
    public static final double DURACION_FIN = 2;

    // balon
    public static final double RADIO_BALON = 20;
    public static final double GRAVEDAD_BALON = 1100;
    public static final double VELOCIDAD_MAXIMA_BALON = 1800;
    public static final double REBOTE_SUELO = 0.75;
    public static final double REBOTE_PARED = 0.85;
    public static final double REBOTE_TRAVESANO = 0.7;
    public static final double REBOTE_JUGADOR = 0.9;
    public static final double FRICCION_SUELO = 1.4;
    public static final double FACTOR_GRAVEDAD_PODER = 0.12;

    // jugador
    public static final double GRAVEDAD_JUGADOR = 2200;
    public static final double RADIO_CABEZA = 38;
    public static final double ALTURA_CABEZA = 70;
    public static final double RADIO_TORSO = 20;
    public static final double ALTURA_TORSO = 22;
    public static final double ALTURA_CADERA = 30;
    public static final double LARGO_PIERNA = 30;
    public static final double RADIO_PIE = 15;

    // patada
    public static final double DURACION_PATADA = 0.30;
    public static final double ENFRIAMIENTO_PATADA = 0.15;
    public static final double GRADOS_INICIO_PATADA = -80;
    public static final double GRADOS_FIN_PATADA = 100;
    public static final double FUERZA_PATADA = 900;

    // estadisticas
    public static final int NIVEL_MINIMO = 1;
    public static final int NIVEL_MAXIMO = 10;
    public static final double INCREMENTO_POR_NIVEL = 0.10;
    public static final double VELOCIDAD_BASE = 330;
    public static final double IMPULSO_SALTO_BASE = 800;
    public static final double VIDA_BASE = 100;
    public static final double DANIO_PATADA_BASE = 10;

    // aturdimiento
    public static final double DURACION_ATURDIMIENTO = 3;

    // poder y barra
    public static final double PODER_MAXIMO = 100;
    public static final double SEGUNDOS_RECARGA_PODER = 10;
    public static final double FRACCION_CARGA_POR_GOL_RECIBIDO = 0.25;
    public static final double INCLINACION_PODER = -0.08;

    // efectos de los tiros de poder
    public static final double DURACION_EMPUJE = 1.5;
    public static final double DURACION_ELECTROCUCION = 3;
    public static final double DURACION_INCONSCIENTE = 3;
    public static final double DURACION_ILUSION = 2.5;
    public static final double VELOCIDAD_EMPUJE = 650;
    public static final double IMPULSO_INCONSCIENTE = 850;
    public static final int BALONES_ILUSION = 8;
}

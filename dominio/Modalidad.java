package dominio;

/**
 * Enum Modalidad
 * Responsable de guardar las posibles modalidades que ofrecemos en el juego
 * 
 * @author Bryan Pinzon y Valentina Prieto
 * @version Entrega Final Proyecto Final
 */

public enum Modalidad {
    //jugador contra jugador (player vs player)
    PVP("Jugador vs jugador", true, true),
    // jugador contra maquina (player  vs machine)
    PVM("Jugador vs maquina", true, false);

    private final String nombre;
    private final boolean humanoIzquierdo;
    private final boolean humanoDerecho;

    /**
     * Constructor
     * @param nombre que es el nombre de la modalidd como string
     * @param humanoIzquierdo true si es un humano(PVP)
     * @param humanoDerecho true si es un humano(PVP) false si es una maquina(PVM)
     */
    Modalidad(String nombre, boolean humanoIzquierdo, boolean humanoDerecho) {
        this.nombre = nombre;
        this.humanoIzquierdo = humanoIzquierdo;
        this.humanoDerecho = humanoDerecho;
    }

    /**
     * Metodo que nos da el nombre de la modalidad
     * @return el nombre que es un string
     */
    public String getNombre(){ 
        return nombre; 
    }

    /**
     * Metodo que nos dice si en un lado esta jugando un humano
     * @param lado que es el lado que queremos consultar
     * @return true si en ese lado se encuentra un humano jugando
     */
    public boolean esHumano(Lado lado){ 
        return lado == Lado.IZQUIERDO ? humanoIzquierdo : humanoDerecho; 
    }

    @Override
    public String toString() { 
        return nombre; 
    }
}

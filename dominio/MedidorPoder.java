package dominio;

import java.io.Serializable;

/**
 * Clase MedidorPoder
 * Responsable de la barra de poder
 * cuando un jugador le meten un gol su barra sube 1/4
 * 
 * es Serializable para poder guardarlo y abrirlo
 * 
 * @author Bryan Pinzon y Valentina Prieto
 * @version Entrega Final Proyecto Final
 */

public class MedidorPoder implements Serializable {
    // identificador de serializacion
    private static final long serialVersionUID = 1L;
    private double carga;

    /**
     * Metodo que recartga la barra respecto al tiempo
     * @param dt
     */
    public void recargar(double dt){
        carga = Math.min(Reglas.PODER_MAXIMO, carga + Reglas.PODER_MAXIMO / Reglas.SEGUNDOS_RECARGA_PODER * dt);
    }

    /**
     * Metodo que recarga la barra al recibir un gol
     */
    public void sumarGolRecibido(){
        carga = Math.min(Reglas.PODER_MAXIMO, carga + Reglas.PODER_MAXIMO * Reglas.FRACCION_CARGA_POR_GOL_RECIBIDO);
    }

    /**
     * Metodo que vacia la barra
     */
    public void vaciar(){ 
        carga = 0; 
    }
    /**
     * Metodo que nos dice si la barra esta llena
     * @return true si ya esta al maximo
     */
    public boolean estaLleno(){ 
        return carga >= Reglas.PODER_MAXIMO; 
    }
    /**
     * Metodo que nos da el porcentaje de la carga
     * @return
     */
    public double getPorcentaje(){ 
        return carga; 
    }
}

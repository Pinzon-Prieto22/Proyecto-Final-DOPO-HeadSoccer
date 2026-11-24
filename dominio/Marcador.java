package dominio;

import java.io.Serializable;

/**
 * Clase Marcador
 * Responsable del marcador del partido (goles marcados)
 * 
 * es Serializable para poder guardarlo y abrirlo
 * 
 * @author Bryan Pinzon y Valentina Prieto
 * @version Entrega Final Proyecto Final
 */

public class Marcador implements Serializable {
    // identificador de serializacion
    private static final long serialVersionUID = 1L;
    //guardamos los goles [gp1,gp2]
    private final int[] goles = new int[2];

    /**
     * Metodo para sumar el gol al anotador respectivamente
     * @param anotador que es el lado que anoto el gol
     */ 
    public void sumarGol(Lado anotador){ 
        //le sumamos 1 a su respectiva posicion
        goles[anotador.ordinal()]++; // con ordinal consideramos su posicion de Enum
    }
    /**
     * Metodo que nos da los goles de un lado especifico
     * @param lado
     * @return
     */
    public int getGoles(Lado lado){ 
        return goles[lado.ordinal()]; // con ordinal consideramos su posicion de Enum
    }
}

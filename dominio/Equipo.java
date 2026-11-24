package dominio;

import java.io.Serializable;

/**
 * Clase Equipo
 * Responsable del pais (equipo) que representa un personaje
 * 
 * es Serializable para poder guardarlo y abrirlo
 * 
 * @author Bryan Pinzon y Valentina Prieto
 * @version Entrega Final Proyecto Final
 */

public class Equipo implements Serializable {
    //Identificador de serializacion
    private static final long serialVersionUID = 1L;
    private final String codigo;
    private final String pais;

    /**
     * Constructor del equipo
     * @param codigo
     * @param pais
     */
    public Equipo(String codigo, String pais) { 
        this.codigo = codigo; 
        this.pais = pais; 
    }
    
    //metodos getter
    public String getCodigo() { 
        return codigo; 
    }
    public String getPais() { 
        return pais; 
    }

    @Override
    public String toString() { 
        return pais; 
    }
}

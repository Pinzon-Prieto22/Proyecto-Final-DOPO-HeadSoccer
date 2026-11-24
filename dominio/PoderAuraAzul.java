package dominio;

/**
 * Clase PoderAuraAzul extiende de Poder
 * Responsable del poder de Corea: Un tiro recto, horizontal a alta velocidad 
 * que empuja al rival hacia su propio arco
 * Dannio 40
 * 
 * es Serializable para poder guardarlo y abrirlo
 * 
 * @author Bryan Pinzon y Valentina Prieto
 * @version Entrega Final Proyecto Final
 */

public class PoderAuraAzul extends Poder {
    //Identificador de serializacion
    private static final long serialVersionUID = 1L;

    /**
     * Constructor del poder
     */
    public PoderAuraAzul() {
        super("aura_azul", "Aura Azul", "Tiro recto y horizontal a alta velocidad. Empuja al rival hacia su propio arco durante 1,5 s.", 40);
    }

    /** 
     * Sobreescribimos el metodo que crea el efecto "empuja al rival"
     * @see dominio.Poder#crearEfecto()
     */
    @Override 
    public Efecto crearEfecto() { 
        return new EfectoEmpuje(); 
    }
    /** 
     * Sobreescribimos el metodo que nos da la velocidad de este tiro
     * @see dominio.Poder#getVelocidadTiro()
     */
    @Override 
    protected double getVelocidadTiro() { 
        return 1500; 
    }
    /**
     * Sobreescribimos el metodo que nos da la inclinacion del tiro (0 porque es horizontal)
     * @see dominio.Poder#getInclinacion()
     */
    @Override 
    protected double getInclinacion() { 
        return 0; 
    }
}

package dominio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Clase CatalogoTiposPartida
 * Responsable del registro de los tipo de partidas disponibles
 * Para agregar un nuevo tipo lo ponemos en una linea
 * 
 * @author Bryan Pinzon y Valentina Prieto
 * @version Entrega Final Proyecto Final
 */

public final class CatalogoTiposPartida {
    private static final CatalogoTiposPartida INSTANCIA = new CatalogoTiposPartida();
    private final List<FabricaTipoPartida> tipos = new ArrayList<FabricaTipoPartida>();

    /**
     * Constructor del catalogo
     */
    private CatalogoTiposPartida() {
        registrar(new FabricaPorTiempo());
        registrar(new FabricaPorGoles());
    }

    /**
     * Metodo que nos da el catalogo
     * @return
     */
    public static CatalogoTiposPartida getInstancia(){ 
        return INSTANCIA; 
    }

    /**
     * metodo que agrega los tipos
     * @param f
     */
    public void registrar(FabricaTipoPartida f) { 
        tipos.add(f); 
    }
    /**
     * Metodo que nos da todos los tipos
     * @return
     */
    public List<FabricaTipoPartida> getTodos(){ 
        return Collections.unmodifiableList(tipos); 
    }
}

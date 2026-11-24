package dominio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * registro de los tipos de partida disponibles
 */
public final class CatalogoTiposPartida {
    private static final CatalogoTiposPartida INSTANCIA = new CatalogoTiposPartida();
    private final List<FabricaTipoPartida> tipos = new ArrayList<FabricaTipoPartida>();

    private CatalogoTiposPartida() {
        registrar(new FabricaPorTiempo());
        registrar(new FabricaPorGoles());
    }

    public static CatalogoTiposPartida getInstancia() { 
        return INSTANCIA;
    }

    public void registrar(FabricaTipoPartida f) {
        tipos.add(f); 
    }
    public List<FabricaTipoPartida> getTodos() { 
        return Collections.unmodifiableList(tipos); 
    }
}

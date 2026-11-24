package dominio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Registro de perfiles de máquina. Una máquina nueva se agrega con una línea (y una subclase de ControladorIA). */
public final class CatalogoMaquinas {
    private static final CatalogoMaquinas INSTANCIA = new CatalogoMaquinas();
    private final List<FabricaMaquina> maquinas = new ArrayList<FabricaMaquina>();

    private CatalogoMaquinas() {
        registrar(new FabricaMaquina("torpe", "Torpe", "Escoge al azar sus movimientos.", MaquinaTorpe::new));
        registrar(new FabricaMaquina("ofensiva", "Ofensiva", "Su prioridad es anotar: persigue el balón sin descanso.", MaquinaOfensiva::new));
        registrar(new FabricaMaquina("defensiva", "Defensiva", "Su prioridad es defender el arco y esquivar tiros de poder.", MaquinaDefensiva::new));
        registrar(new FabricaMaquina("experta", "Experta", "Decide la acción según el estado del partido (marcador, tiempo y vida).", MaquinaExperta::new));
    }

    public static CatalogoMaquinas getInstancia() { return INSTANCIA; }

    public void registrar(FabricaMaquina f) { maquinas.add(f); }
    public List<FabricaMaquina> getTodas() { return Collections.unmodifiableList(maquinas); }
}

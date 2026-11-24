package dominio;

import java.util.function.Supplier;

/** Describe un perfil de máquina seleccionable y sabe crear su controlador. */
public class FabricaMaquina {
    private final String id;
    private final String nombre;
    private final String descripcion;
    private final Supplier<ControladorIA> creador;

    public FabricaMaquina(String id, String nombre, String descripcion, Supplier<ControladorIA> creador) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.creador = creador;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public ControladorIA crear() { return creador.get(); }

    @Override
    public String toString() { return nombre; }
}

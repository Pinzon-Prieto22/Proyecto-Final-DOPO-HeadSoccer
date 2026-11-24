package dominio;

/** Efecto con duración fija. Es neutro por defecto: cada subclase sobrescribe solo lo que cambia. */
public abstract class EfectoTemporal implements Efecto {
    private static final long serialVersionUID = 1L;
    private final String id;
    private final String nombre;
    private final double duracion;
    private double restante;

    protected EfectoTemporal(String id, String nombre, double duracion) {
        this.id = id;
        this.nombre = nombre;
        this.duracion = duracion;
        this.restante = duracion;
    }

    @Override public String getId() { return id; }
    @Override public String getNombre() { return nombre; }
    @Override public double getDuracion() { return duracion; }
    @Override public void actualizar(double dt) { restante -= dt; }
    @Override public boolean haTerminado() { return restante <= 0; }
    @Override public double getTiempoRestante() { return Math.max(0, restante); }

    @Override public boolean bloqueaAcciones() { return false; }
    @Override public boolean bloqueaSalto() { return false; }
    @Override public double factorVelocidad() { return 1.0; }
}

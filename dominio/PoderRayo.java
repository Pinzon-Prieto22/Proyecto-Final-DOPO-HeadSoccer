package dominio;

/** Camerún: tiro recto cubierto de electricidad. Electrocuta al rival (no salta y va al 50 %). Daño 60. */
public class PoderRayo extends Poder {
    private static final long serialVersionUID = 1L;

    public PoderRayo() {
        super("rayo", "Rayo", "Tiro recto cubierto de electricidad. El rival queda electrocutado 3 s: no puede saltar y se mueve al 50 %.", 60);
    }

    @Override public Efecto crearEfecto() { return new EfectoElectrocucion(); }
    @Override protected double getVelocidadTiro() { return 1200; }
}

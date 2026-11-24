package dominio;
/**
 * Poder de camerun, tiro recto, electrico y electrocuta al rival, dannio de 60
 */
public class PoderRayo extends Poder {
    public PoderRayo() {
        super("rayo", "Rayo", "Tiro recto cubierto de electricidad. El rival queda electrocutado 3 s: no puede saltar y se mueve al 50 %.", 60);
    }

    @Override public Efecto crearEfecto() { return new EfectoElectrocucion(); }
    @Override protected double getVelocidadTiro() { return 1200; }
}

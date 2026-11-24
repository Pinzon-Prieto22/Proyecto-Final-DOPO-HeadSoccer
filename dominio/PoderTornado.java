package dominio;

/** Nigeria: remolino a ras de suelo que rebota rápidamente. Deja al rival inconsciente 3 s. Daño 40. */
public class PoderTornado extends Poder {
    private static final long serialVersionUID = 1L;

    public PoderTornado() {
        super("tornado", "Tornado", "Remolino a ras de suelo que rebota rápidamente. El rival sale volando y queda inconsciente 3 s.", 40);
    }

    @Override public Efecto crearEfecto() { return new EfectoInconsciente(); }
    @Override protected double getVelocidadTiro() { return 1000; }
    @Override protected double getInclinacion() { return -0.35; }
    @Override public double getFactorGravedad() { return 2.5; }
    @Override public double getReboteSuelo() { return 0.95; }
    @Override public boolean sobreviveRebotesEnSuelo() { return true; }

    @Override
    protected void prepararBalon(Balon balon) { balon.setPosicion(balon.getX(), Reglas.Y_SUELO - balon.getRadio()); }
}

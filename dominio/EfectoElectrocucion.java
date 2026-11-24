package dominio;

/** Electrocutado (Camerún): no puede saltar y se mueve al 50 % de su velocidad durante 3 s. */
public class EfectoElectrocucion extends EfectoTemporal {
    private static final long serialVersionUID = 1L;

    public EfectoElectrocucion() { super("electrocucion", "Electrocutado", Reglas.DURACION_ELECTROCUCION); }

    @Override public boolean bloqueaSalto() { return true; }
    @Override public double factorVelocidad() { return 0.5; }
}

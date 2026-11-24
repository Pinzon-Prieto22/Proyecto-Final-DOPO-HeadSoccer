package dominio;

/** Empuje (Corea del Sur): el rival es lanzado hacia su propio arco durante 1,5 s. */
public class EfectoEmpuje extends EfectoTemporal {
    private static final long serialVersionUID = 1L;

    public EfectoEmpuje() { super("empuje", "Empujado", Reglas.DURACION_EMPUJE); }

    @Override
    public void enCadaPaso(Jugador victima, double dt) {
        double fuerza = Reglas.VELOCIDAD_EMPUJE * (getTiempoRestante() / getDuracion());
        victima.fijarImpulso(-victima.getDireccion() * fuerza);   // hacia SU arco (a su espalda)
    }
}

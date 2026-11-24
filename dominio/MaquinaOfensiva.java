package dominio;

import java.util.EnumSet;

/** Ofensiva: su prioridad es anotar. Persigue siempre el balón y usa el poder apenas puede. */
public class MaquinaOfensiva extends ControladorIA {
    private static final long serialVersionUID = 1L;

    public MaquinaOfensiva() { super(0.15, 25); }

    @Override
    protected void elegirAcciones(Jugador yo, Partida p, EnumSet<Accion> a) {
        Balon b = p.getBalon();
        atacarBalon(yo, b, a);
        usarPoderSiConviene(yo, b, a);
    }
}

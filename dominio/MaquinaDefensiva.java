package dominio;

import java.util.EnumSet;

/** Defensiva: su prioridad es defender el arco. Esquiva los tiros de poder y solo sale cuando el balón está en su mitad. */
public class MaquinaDefensiva extends ControladorIA {
    private static final long serialVersionUID = 1L;

    public MaquinaDefensiva() { super(0.15, 25); }

    @Override
    protected void elegirAcciones(Jugador yo, Partida p, EnumSet<Accion> a) {
        Balon b = p.getBalon();
        defender(yo, b, p, a);
        esquivarTiro(yo, b, a);
        usarPoderSiConviene(yo, b, a);
    }
}

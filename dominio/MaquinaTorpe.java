package dominio;

import java.util.EnumSet;

/** Torpe: escoge al azar sus movimientos. */
public class MaquinaTorpe extends ControladorIA {
    private static final long serialVersionUID = 1L;

    public MaquinaTorpe() { super(0.35, 0); }

    @Override
    protected void elegirAcciones(Jugador yo, Partida p, EnumSet<Accion> a) {
        int d = azar.nextInt(3);
        if (d == 1) a.add(Accion.IZQUIERDA); else if (d == 2) a.add(Accion.DERECHA);
        if (azar.nextDouble() < 0.15) a.add(Accion.SALTAR);
        if (azar.nextDouble() < 0.25) a.add(Accion.PATEAR);
        if (yo.getMedidor().estaLleno() && !yo.tieneAura() && azar.nextDouble() < 0.4) a.add(Accion.PODER);
    }
}

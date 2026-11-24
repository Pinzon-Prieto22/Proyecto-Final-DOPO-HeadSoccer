package dominio;

import java.util.EnumSet;

/**
 * Experta: decide según el estado del partido. Si va ganando y el final se acerca (o tiene poca vida), defiende;
 * si no, ataca. Esquiva tiros de poder, castiga con patadas al rival cercano y aprovecha cuando el rival está aturdido.
 */
public class MaquinaExperta extends ControladorIA {
    private static final long serialVersionUID = 1L;

    public MaquinaExperta() { super(0.07, 8); }

    @Override
    protected void elegirAcciones(Jugador yo, Partida p, EnumSet<Accion> a) {
        Balon b = p.getBalon();
        Jugador rival = rival(yo, p);
        int mios = p.getMarcador().getGoles(yo.getLado());
        int suyos = p.getMarcador().getGoles(rival.getLado());
        double restante = p.getTipo().getTiempoRestante();
        boolean finalCercano = restante >= 0 && restante < 20;
        boolean pocaVida = yo.getVida() < yo.getVidaMaxima() * 0.3;
        boolean proteger = !rival.estaAturdido() && ((mios > suyos && finalCercano) || (pocaVida && mios >= suyos));

        if (proteger && !yo.tieneAura()) defender(yo, b, p, a);
        else atacarBalon(yo, b, a);

        esquivarTiro(yo, b, a);
        boolean balonCerca = Math.abs(b.getX() - yo.getX()) < 120;
        if (!balonCerca && rivalAlAlcance(yo, rival)) a.add(Accion.PATEAR);
        usarPoderSiConviene(yo, b, a);
    }
}

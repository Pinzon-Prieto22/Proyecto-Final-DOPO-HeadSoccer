package presentacion;

import java.awt.Color;
/**
 * Colores de cada poder
 */
public final class AspectoPoder {
    private AspectoPoder() { }

    public static Color color(String idPoder) {
        if (idPoder == null) return Color.WHITE;
        if (idPoder.equals("aura_azul")) return new Color(70, 150, 255);
        if (idPoder.equals("rayo")) return new Color(255, 235, 60);
        if (idPoder.equals("tornado")) return new Color(185, 200, 215);
        if (idPoder.equals("ilusion")) return new Color(190, 100, 240);
        return Color.WHITE;
    }
}

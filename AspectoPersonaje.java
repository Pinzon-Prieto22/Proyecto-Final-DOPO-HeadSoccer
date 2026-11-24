package presentacion;

import java.awt.Color;
import dominio.Personaje;
/**
 * Clase con los colores de cada personaje
 */
public final class AspectoPersonaje {
    public final Color camisa, pantaloneta, piel, cabello, zapato;

    private AspectoPersonaje(Color camisa, Color pantaloneta, Color piel, Color cabello) {
        this.camisa = camisa;
        this.pantaloneta = pantaloneta;
        this.piel = piel;
        this.cabello = cabello;
        this.zapato = new Color(35, 30, 30);
    }

    public static AspectoPersonaje de(Personaje p) {
        String c = p.getId();
        if (c.equals("kor")) return new AspectoPersonaje(new Color(205, 30, 55), new Color(30, 40, 110), new Color(247, 218, 185), new Color(15, 15, 20));
        if (c.equals("cmr")) return new AspectoPersonaje(new Color(0, 150, 70), new Color(205, 30, 45), new Color(125, 80, 55), new Color(15, 15, 15));
        if (c.equals("nga")) return new AspectoPersonaje(new Color(0, 135, 80), Color.WHITE, new Color(105, 68, 45), new Color(20, 15, 15));
        if (c.equals("usa")) return new AspectoPersonaje(new Color(30, 60, 150), new Color(235, 235, 240), new Color(240, 200, 170), new Color(205, 160, 60));
        return new AspectoPersonaje(new Color(120, 120, 140), new Color(60, 60, 80), new Color(230, 190, 160), new Color(60, 40, 30));
    }
}

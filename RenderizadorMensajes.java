package presentacion;

import java.awt.Color;
import java.awt.Graphics2D;
import dominio.Partida;
import dominio.Reglas;
/**
 * Clase que dibuja los mensajes bonitos
 */
public class RenderizadorMensajes implements Renderizador {
    @Override
    public void dibujar(Graphics2D g, Partida p) {
        double cx = Reglas.ANCHO / 2;
        switch (p.getEstado()) {
            case CUENTA_REGRESIVA: {
                int n = (int) Math.ceil(p.getTemporizador());
                grande(g, n > 0 ? String.valueOf(n) : "\u00a1YA!", cx, 270, 130, Estilos.DORADO);
                break;
            }
            case GOL: {
                grande(g, p.isUltimoAutogol() ? "\u00a1AUTOGOL!" : "\u00a1GOL!", cx, 250, 120, new Color(255, 225, 60));
                String quien = p.getJugador(p.getUltimoAnotador()).getNombre();
                Estilos.textoContorno(g, "Punto para " + quien, Estilos.fuente(30), cx, 310, Color.WHITE, Color.BLACK, 6f);
                break;
            }
            case FIN_PARTIDO:
                grande(g, "\u00a1FIN!", cx, 260, 120, Color.WHITE);
                break;
            case JUGANDO:
                if (p.getTipo().enMuerteSubita() && (System.currentTimeMillis() / 400) % 2 == 0) {
                    Estilos.textoContorno(g, "\u00a1MUERTE S\u00daBITA!", Estilos.fuenteTitulo(40), cx, 140, new Color(255, 215, 0), new Color(120, 40, 0), 8f);
                }
                break;
            default:
                break;
        }
    }

    private void grande(Graphics2D g, String s, double cx, double y, int tam, Color relleno) {
        Estilos.textoContorno(g, s, Estilos.fuenteTitulo(tam), cx, y, relleno, Estilos.MARRON, 12f);
    }
}

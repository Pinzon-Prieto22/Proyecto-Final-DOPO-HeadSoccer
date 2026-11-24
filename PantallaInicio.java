package presentacion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * Pantalla de inicio
 */
public class PantallaInicio extends PantallaBase {
    public PantallaInicio(final Navegador navegador) {
        super(navegador);
        BotonDorado jugar = new BotonDorado("JUGAR");
        BotonDorado ayuda = new BotonDorado("C\u00d3MO JUGAR");
        BotonDorado salir = new BotonDorado("SALIR");
        jugar.addActionListener(e -> { navegador.getContexto().nuevaConfiguracion(); navegador.ir(IdPantalla.CONFIGURACION); });
        ayuda.addActionListener(e -> navegador.ir(IdPantalla.AYUDA));
        salir.addActionListener(e -> System.exit(0));

        JPanel botones = new JPanel(new GridLayout(0, 1, 0, 14));
        botones.setOpaque(false);
        for (BotonDorado b : new BotonDorado[] { jugar, ayuda, salir }) {
            b.setPreferredSize(new Dimension(320, 54));
            botones.add(b);
        }
        JPanel abajo = new JPanel(new GridBagLayout());
        abajo.setOpaque(false);
        abajo.setBorder(new EmptyBorder(0, 0, 40, 0));
        abajo.add(botones);
        add(abajo, BorderLayout.SOUTH);
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0.create();
        Estilos.activarCalidad(g);
        int cx = getWidth() / 2;
        int tam = Math.max(48, Math.min(getWidth() / 9, 110));
        Estilos.textoContorno(g, "DOPO", Estilos.fuenteTitulo(tam), cx, 40 + tam, new Color(255, 226, 90), Estilos.MARRON, tam / 6f);
        Estilos.textoContorno(g, "SOCCER", Estilos.fuenteTitulo((int) (tam * 0.95)), cx, 40 + tam * 2.0, Color.WHITE, Estilos.MARRON, tam / 6f);
        g.dispose();
    }

    @Override public IdPantalla getId() { return IdPantalla.INICIO; }
}

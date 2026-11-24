package presentacion;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.util.EnumMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Ventana única de la aplicación (JFrame). Contiene un JPanel con CardLayout que apila todas las pantallas
 * y muestra solo una a la vez. Ajusta su tamaño a la resolución de cancha elegida.
 */
public class VentanaPrincipal extends JFrame implements Navegador {
    private final CardLayout cartas = new CardLayout();
    private final JPanel contenedor = new JPanel(cartas);
    private final Map<IdPantalla, Pantalla> pantallas = new EnumMap<IdPantalla, Pantalla>(IdPantalla.class);
    private final ContextoJuego contexto;
    private Pantalla actual;

    public VentanaPrincipal(ContextoJuego contexto) {
        super("DOPOSoccer");
        this.contexto = contexto;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        registrar(new PantallaInicio(this));
        registrar(new PantallaConfiguracion(this));
        registrar(new PantallaSeleccion(this));
        registrar(new PantallaJuego(this));
        registrar(new PantallaFinPartida(this));
        registrar(new PantallaAyuda(this));
        setContentPane(contenedor);
        setMinimumSize(new Dimension(760, 520));
        ajustarTamano(1100, 720);
    }

    private void registrar(Pantalla p) {
        pantallas.put(p.getId(), p);
        contenedor.add(p.getVista(), p.getId().name());
    }

    public void iniciar() {
        ir(IdPantalla.INICIO);
        setVisible(true);
    }

    @Override
    public void ir(IdPantalla destino) {
        if (actual != null) actual.alOcultar();
        actual = pantallas.get(destino);
        cartas.show(contenedor, destino.name());
        actual.alMostrar();
    }

    @Override public ContextoJuego getContexto() { return contexto; }
    @Override public JFrame getVentana() { return this; }

    @Override
    public void ajustarTamano(int ancho, int alto) {
        Rectangle pantalla = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        int w = Math.min(ancho, pantalla.width - 20), h = Math.min(alto, pantalla.height - 50);
        contenedor.setPreferredSize(new Dimension(w, h));
        pack();
        setLocationRelativeTo(null);
    }
}

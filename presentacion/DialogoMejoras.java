package presentacion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.util.EnumMap;
import java.util.Map;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import dominio.DOPOSoccerException;
import dominio.Estadisticas;
import dominio.Perfil;
import dominio.RegistroPerfiles;
import dominio.Reglas;
import dominio.TipoEstadistica;

/** Mejora de estadísticas con los puntos ganados: cada punto de mejora cuesta el doble del anterior (500, 1000, 2000...). */
public class DialogoMejoras extends JDialog {
    private final RegistroPerfiles registro;
    private final Perfil perfil;
    private final Runnable alCambiar;
    private final JLabel lblPuntos = new JLabel(" ");
    private final Map<TipoEstadistica, BarraEstadistica> barras = new EnumMap<TipoEstadistica, BarraEstadistica>(TipoEstadistica.class);
    private final Map<TipoEstadistica, JLabel> efectos = new EnumMap<TipoEstadistica, JLabel>(TipoEstadistica.class);
    private final Map<TipoEstadistica, JLabel> precios = new EnumMap<TipoEstadistica, JLabel>(TipoEstadistica.class);

    public DialogoMejoras(Window padre, RegistroPerfiles registro, Perfil perfil, Runnable alCambiar) {
        super(padre, "Mejorar estad\u00edsticas \u2014 " + perfil.getNombre(), ModalityType.APPLICATION_MODAL);
        this.registro = registro;
        this.perfil = perfil;
        this.alCambiar = alCambiar;

        JPanel contenido = new JPanel(new BorderLayout(10, 10));
        contenido.setBackground(Estilos.AZUL_OSCURO);
        contenido.setBorder(new EmptyBorder(16, 20, 16, 20));

        lblPuntos.setFont(Estilos.fuente(20));
        lblPuntos.setForeground(Estilos.DORADO);
        contenido.add(lblPuntos, BorderLayout.NORTH);

        JPanel filas = new JPanel(new GridBagLayout());
        filas.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 8, 6, 8);
        c.anchor = GridBagConstraints.WEST;
        int y = 0;
        for (final TipoEstadistica t : TipoEstadistica.values()) {
            JLabel nombre = new JLabel("<html><b>" + t.getNombre() + "</b><br><font size='-1'>" + t.getDescripcion() + "</font></html>");
            nombre.setForeground(Color.WHITE);
            BarraEstadistica barra = new BarraEstadistica("Nivel");
            barra.setPreferredSize(new Dimension(220, 22));
            JLabel efecto = new JLabel(" ");
            efecto.setForeground(new Color(150, 220, 255));
            JLabel precio = new JLabel(" ");
            precio.setForeground(Color.WHITE);
            BotonDorado comprar = new BotonDorado("MEJORAR");
            comprar.setPreferredSize(new Dimension(130, 38));
            comprar.setFont(Estilos.fuente(14));
            comprar.addActionListener(e -> comprar(t));
            barras.put(t, barra); efectos.put(t, efecto); precios.put(t, precio);
            c.gridy = y++;
            c.gridx = 0; c.weightx = 0; filas.add(nombre, c);
            c.gridx = 1; filas.add(barra, c);
            c.gridx = 2; c.weightx = 1; filas.add(efecto, c);
            c.gridx = 3; c.weightx = 0; filas.add(precio, c);
            c.gridx = 4; filas.add(comprar, c);
        }
        contenido.add(filas, BorderLayout.CENTER);

        BotonDorado cerrar = new BotonDorado("LISTO");
        cerrar.addActionListener(e -> dispose());
        JPanel sur = new JPanel(new FlowLayout(FlowLayout.CENTER));
        sur.setOpaque(false);
        sur.add(cerrar);
        contenido.add(sur, BorderLayout.SOUTH);

        setContentPane(contenido);
        refrescar();
        pack();
        setLocationRelativeTo(padre);
    }

    private void comprar(TipoEstadistica t) {
        try {
            registro.mejorar(perfil, t);
            refrescar();
            alCambiar.run();
        } catch (DOPOSoccerException e) {
            Mensajes.error(this, "No se pudo mejorar " + t.getNombre(), e);
        }
    }

    private void refrescar() {
        lblPuntos.setText("Puntos disponibles: " + perfil.getPuntos());
        Estadisticas s = perfil.getEstadisticas();
        for (TipoEstadistica t : TipoEstadistica.values()) {
            barras.get(t).setValor(s.getNivel(t));
            efectos.get(t).setText(efecto(t, s));
            int precio = s.getPrecioSiguiente(t);
            precios.get(t).setText(precio < 0 ? "M\u00c1XIMO" : "Cuesta " + precio);
        }
    }

    private static String efecto(TipoEstadistica t, Estadisticas s) {
        int n = s.getNivel(t) - Reglas.NIVEL_MINIMO;
        switch (t) {
            case SPEED: return "+" + 10 * n + " % de velocidad";
            case KICK: return "Da\u00f1o \u00d7" + String.format("%.1f", s.getMultiplicadorKick()) + "  (patada " + String.format("%.0f", s.getDanioPatada()) + ")";
            case JUMP: return "+" + 10 * n + " % de altura de salto";
            default: return "Vida m\u00e1xima " + (int) s.getVidaMaxima();
        }
    }
}

package presentacion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Window;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import dominio.DOPOSoccerException;
import dominio.Perfil;
import dominio.RegistroPerfiles;
import dominio.TipoEstadistica;

/** Perfil de un jugador humano: lista de perfiles, nuevo perfil, estadísticas, puntos y botón para mejorarlas. */
public class PanelPerfil extends JPanel {
    private final RegistroPerfiles registro;
    private final JComboBox<Perfil> combo = new JComboBox<Perfil>();
    private final BarraEstadistica[] barras = new BarraEstadistica[TipoEstadistica.values().length];
    private final JLabel lblPuntos = new JLabel(" ");

    public PanelPerfil(RegistroPerfiles registro) {
        super(new BorderLayout(6, 6));
        setOpaque(false);
        this.registro = registro;

        BotonDorado nuevo = new BotonDorado("NUEVO");
        nuevo.setPreferredSize(new Dimension(100, 36));
        nuevo.setFont(Estilos.fuente(13));
        nuevo.addActionListener(e -> nuevoPerfil());
        JLabel perfil = new JLabel("Perfil:");
        perfil.setForeground(Color.WHITE);
        perfil.setFont(Estilos.fuente(14));
        JPanel fila = new JPanel(new BorderLayout(8, 0));
        fila.setOpaque(false);
        fila.add(perfil, BorderLayout.WEST);
        fila.add(combo, BorderLayout.CENTER);
        fila.add(nuevo, BorderLayout.EAST);

        JPanel grid = new JPanel(new GridLayout(2, 2, 10, 2));
        grid.setOpaque(false);
        for (TipoEstadistica t : TipoEstadistica.values()) {
            barras[t.ordinal()] = new BarraEstadistica(t.getNombre());
            grid.add(barras[t.ordinal()]);
        }

        lblPuntos.setForeground(Estilos.DORADO);
        lblPuntos.setFont(Estilos.fuente(15));
        BotonDorado mejorar = new BotonDorado("MEJORAR");
        mejorar.setPreferredSize(new Dimension(150, 36));
        mejorar.setFont(Estilos.fuente(14));
        mejorar.addActionListener(e -> mejorar());
        JPanel pie = new JPanel(new BorderLayout(8, 0));
        pie.setOpaque(false);
        pie.add(lblPuntos, BorderLayout.CENTER);
        pie.add(mejorar, BorderLayout.EAST);

        add(fila, BorderLayout.NORTH);
        add(grid, BorderLayout.CENTER);
        add(pie, BorderLayout.SOUTH);
        combo.addActionListener(e -> refrescar());
    }

    private Window ventana() { return SwingUtilities.getWindowAncestor(this); }

    public Perfil getPerfil() { return (Perfil) combo.getSelectedItem(); }

    /** Vuelve a cargar la lista de perfiles y deja seleccionado 'elegir' (o el primero). */
    public void recargarLista(Perfil elegir) {
        combo.setModel(new DefaultComboBoxModel<Perfil>(registro.getTodos().toArray(new Perfil[0])));
        if (elegir != null) combo.setSelectedItem(elegir);
        refrescar();
    }

    public void setPerfil(Perfil p) { combo.setSelectedItem(p); refrescar(); }

    /** Actualiza las barras y los puntos con el perfil seleccionado. */
    public void refrescar() {
        Perfil p = getPerfil();
        for (TipoEstadistica t : TipoEstadistica.values()) barras[t.ordinal()].setValor(p == null ? 1 : p.getEstadisticas().getNivel(t));
        lblPuntos.setText(p == null ? "Crea un perfil para jugar" : "Puntos: " + p.getPuntos() + "   (ganados " + p.getVictorias() + " \u00b7 perdidos " + p.getDerrotas() + ")");
    }

    private void nuevoPerfil() {
        String nombre = JOptionPane.showInputDialog(ventana(), "Nombre del nuevo perfil (1 a 16 caracteres):", "Nuevo perfil", JOptionPane.QUESTION_MESSAGE);
        if (nombre == null) return;
        try {
            recargarLista(registro.crear(nombre));
        } catch (DOPOSoccerException e) {
            Mensajes.error(ventana(), "No se pudo crear el perfil", e);
        }
    }

    private void mejorar() {
        Perfil p = getPerfil();
        if (p == null) { Mensajes.info(ventana(), "Mejorar estad\u00edsticas", "Primero crea o elige un perfil."); return; }
        new DialogoMejoras(ventana(), registro, p, this::refrescar).setVisible(true);
    }
}
